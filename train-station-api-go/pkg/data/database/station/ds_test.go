package station_test

import (
	"fmt"
	"testing"

	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/data/database/fields"
	"github.com/Darkness4/train-station-api/pkg/data/database/geometry"
	"github.com/Darkness4/train-station-api/pkg/data/database/isfavorite"
	"github.com/Darkness4/train-station-api/pkg/data/database/station"
	"github.com/glebarez/sqlite"
	"github.com/google/uuid"
	"github.com/stretchr/testify/suite"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

func fakeStation() *station.Model {
	return &station.Model{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		Libelle:         "libelle",
		IsFavorites:     make([]*isfavorite.Model, 0),
		RecordTimestamp: "record_timestamp",
		Fields: &fields.Model{
			GeoPoint2D: "[]",
			CGeo:       "[]",
			GeoShape: &geometry.Model{
				Coordinates: "[]",
			},
		},
		Geometry: &geometry.Model{
			Coordinates: "[]",
		},
	}
}

type DataSourceTestSuite struct {
	suite.Suite
	db   *gorm.DB
	impl *station.DataSourceImpl
}

func (suite *DataSourceTestSuite) BeforeTest(suiteName, testName string) {
	db, err := gorm.Open(sqlite.Open(fmt.Sprintf("file:%s?mode=memory&cache=shared", uuid.NewString())), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Silent),
	})
	if err != nil {
		internal.Logger.Panic(err.Error())
	}
	if err := db.AutoMigrate(
		&station.Model{},
		&isfavorite.Model{},
	); err != nil {
		internal.Logger.Panic(err.Error())
	}
	if err := db.Exec("PRAGMA foreign_keys = ON").Error; err != nil {
		internal.Logger.Panic(err.Error())
	}
	suite.db = db
	suite.impl = station.NewDataSource(db)
}

func (suite *DataSourceTestSuite) AfterTest(suiteName, testName string) {
	sqlDB, _ := suite.db.DB()
	if sqlDB != nil {
		sqlDB.Close()
		suite.db = nil
	}
}

func (suite *DataSourceTestSuite) TestCreateStation() {
	in := fakeStation()

	_, err := suite.impl.CreateStation(in)
	suite.NoError(err)

	var count int64
	suite.db.Model(&station.Model{}).Count(&count)
	suite.EqualValues(count, 1)
}

func (suite *DataSourceTestSuite) TestCreateStationNonWorking() {
	var nonWorkingTable = []struct {
		name string
		in   *station.Model
	}{
		{"Empty Object", &station.Model{}},
		{"First layer filled", &station.Model{
			RecordID:        "recordid",
			DatasetID:       "datasetid",
			Libelle:         "libelle",
			RecordTimestamp: "record_timestamp",
			Fields:          &fields.Model{},
			Geometry:        &geometry.Model{},
		}},
	}

	for _, tt := range nonWorkingTable {
		suite.Run(fmt.Sprintf("%s should not work", tt.name), func() {
			_, err := suite.impl.CreateStation(tt.in)
			suite.Error(err)
		})
	}
}

func (suite *DataSourceTestSuite) TestCreateManyStation() {
	in := []*station.Model{
		fakeStation(),
	}

	_, err := suite.impl.CreateManyStation(in)
	suite.NoError(err)

	var count int64
	suite.db.Model(&station.Model{}).Count(&count)
	suite.EqualValues(count, 1)
}

func (suite *DataSourceTestSuite) TestUpdateStation() {
	// Arrange
	err := suite.db.Create(fakeStation()).Error
	suite.NoError(err)
	in := &station.Model{
		Libelle: "updated",
	}

	// Act
	_, err = suite.impl.UpdateStation("recordid", in)
	suite.NoError(err)

	// Assert
	m := &station.Model{
		RecordID: "recordid",
	}
	err = suite.db.First(m).Error
	suite.NoError(err)
	suite.Equal(in.Libelle, m.Libelle)
}

func (suite *DataSourceTestSuite) TestFindOneStation() {
	// Arrange
	err := suite.db.Create(fakeStation()).Error
	suite.NoError(err)

	// Act
	got, err := suite.impl.FindOneStation("recordid")
	suite.NoError(err)
	suite.NotNil(got)
}

func (suite *DataSourceTestSuite) TestFindManyAndCountStation() {
	// Arrange
	mock := fakeStation()
	err := suite.db.Create(mock).Error
	suite.NoError(err)
	mockIsFavorite := &isfavorite.Model{
		UserID:    "userId",
		StationID: mock.RecordID,
	}
	err = suite.db.Create(mockIsFavorite).Error
	suite.NoError(err)

	// Act
	got, count, err := suite.impl.FindManyAndCountStation("", 0, 0)
	suite.NoError(err)
	suite.NotNil(got)
	suite.EqualValues(1, count)
	suite.EqualValues(1, len(got))
	suite.EqualValues(1, len(got[0].IsFavorites))
	suite.Equal(got[0].IsFavorites[0].UserID, "userId")
}

func (suite *DataSourceTestSuite) TestCountStation() {
	// Arrange
	mock := fakeStation()
	err := suite.db.Create(mock).Error
	suite.NoError(err)

	suite.Run("should work", func() {
		// Act
		count, err := suite.impl.CountStation("")
		suite.NoError(err)
		suite.EqualValues(1, count)
	})

	suite.Run("should work with cont filter", func() {
		// Act
		count, err := suite.impl.CountStation("libelle")
		suite.NoError(err)
		suite.EqualValues(1, count)
	})

	suite.Run("should work with wrong cont filter", func() {
		// Act
		count, err := suite.impl.CountStation("notlib")
		suite.NoError(err)
		suite.EqualValues(0, count)
	})
}

func (suite *DataSourceTestSuite) TestCreateIsFavorite() {
	var workingTable = []struct {
		name string
		mock *station.Model
		in   *isfavorite.Model
	}{
		{"CreateIsFavorite with StationID only",
			&station.Model{
				RecordID:        "recordid",
				DatasetID:       "datasetid",
				Libelle:         "libelle",
				RecordTimestamp: "record_timestamp",
				Fields: &fields.Model{
					GeoPoint2D: "[]",
					CGeo:       "[]",
					GeoShape: &geometry.Model{
						Coordinates: "[]",
					},
				},
				Geometry: &geometry.Model{
					Coordinates: "[]",
				},
			},
			&isfavorite.Model{
				UserID:    "userId",
				StationID: "recordid",
			},
		},
	}

	// Tests
	for _, tt := range workingTable {
		suite.Run(fmt.Sprintf("%s should work", tt.name), func() {
			suite.T().Cleanup(func() {
				suite.db.Where("1 = 1").Delete(&station.Model{})
				suite.db.Where("1 = 1").Delete(&isfavorite.Model{})
			})

			_, err := suite.impl.CreateStation(tt.mock)
			suite.NoError(err)

			got, err := suite.impl.CreateIsFavorite(tt.in)
			suite.NoError(err)

			suite.Equal(tt.mock.RecordID, got.StationID)

			var count int64
			suite.db.Model(&isfavorite.Model{}).Count(&count)
			suite.EqualValues(1, count)

			testModel := station.Model{
				RecordID: "recordid",
			}
			suite.db.Preload("IsFavorites").First(&testModel)
			suite.EqualValues(1, len(testModel.IsFavorites))
			suite.Equal("userId", (*testModel.IsFavorites[0]).UserID)
		})
	}
}

func (suite *DataSourceTestSuite) TestCreateIsFavoriteNonWorking() {
	var nonWorkingTable = []struct {
		name string
		in   *isfavorite.Model
	}{
		{"Empty Object", &isfavorite.Model{}},
		{"Non existing relation", &isfavorite.Model{
			UserID:    "fakeid",
			StationID: "fakerelation",
		}},
	}

	for _, tt := range nonWorkingTable {
		suite.Run(fmt.Sprintf("%s should not work", tt.name), func() {
			_, err := suite.impl.CreateIsFavorite(tt.in)
			suite.Error(err)
		})
	}
}

func (suite *DataSourceTestSuite) TestRemoveIsFavorite() {
	var workingTable = []struct {
		name string
		mock *station.Model
		in   *isfavorite.Model
	}{
		{"RemoveIsFavorite with StationID only",
			&station.Model{
				RecordID:        "recordid",
				DatasetID:       "datasetid",
				Libelle:         "libelle",
				RecordTimestamp: "record_timestamp",
				Fields: &fields.Model{
					GeoPoint2D: "[]",
					CGeo:       "[]",
					GeoShape: &geometry.Model{
						Coordinates: "[]",
					},
				},
				Geometry: &geometry.Model{
					Coordinates: "[]",
				},
			},
			&isfavorite.Model{
				UserID:    "userId",
				StationID: "recordid",
			},
		},
	}

	// Tests
	for _, tt := range workingTable {
		suite.Run(fmt.Sprintf("%s should work", tt.name), func() {
			suite.T().Cleanup(func() {
				suite.db.Where("1 = 1").Delete(&station.Model{})
				suite.db.Where("1 = 1").Delete(&isfavorite.Model{})
			})

			_, err := suite.impl.CreateStation(tt.mock)
			suite.NoError(err)

			_, err = suite.impl.CreateIsFavorite(tt.in)
			suite.NoError(err)

			err = suite.impl.RemoveIsFavorite(tt.in)
			suite.NoError(err)

			var count int64
			suite.db.Model(&isfavorite.Model{}).Count(&count)
			suite.EqualValues(0, count)
		})
	}
}

func TestDataSourceTestSuite(t *testing.T) {
	suite.Run(t, new(DataSourceTestSuite))
}
