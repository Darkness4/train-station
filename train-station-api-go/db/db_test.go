package db_test

import (
	"context"
	"database/sql"
	"fmt"
	"testing"

	"github.com/Darkness4/train-station-api/db"
	"github.com/Darkness4/train-station-api/db/models"
	"github.com/Darkness4/train-station-api/db/types"
	"github.com/Darkness4/train-station-api/logger"
	"github.com/golang-migrate/migrate/v4"
	"github.com/golang-migrate/migrate/v4/database/sqlite"
	_ "github.com/golang-migrate/migrate/v4/source/file"
	"github.com/google/uuid"
	"github.com/stretchr/testify/suite"
	"github.com/volatiletech/sqlboiler/v4/boil"
	"go.uber.org/zap"
	_ "modernc.org/sqlite"
)

func fakeStation(id string) *models.Station {
	return &models.Station{
		ID:         id,
		Commune:    "commune",
		YWGS84:     1.0,
		XWGS84:     1.0,
		Libelle:    "libelle",
		Idgaia:     "idgaia",
		Voyageurs:  "voyageurs",
		GeoPoint2D: "geopoint2d",
		CodeLigne:  "codeligne",
		XL93:       1.0,
		CGeo:       "cgeo",
		RGTroncon:  1,
		GeoShape:   "geoshape",
		PK:         "pk",
		Idreseau:   1,
		Departemen: "departement",
		YL93:       1.0,
		Fret:       "fret",
	}
}

type DBTestSuite struct {
	suite.Suite
	db *sql.DB
}

func (suite *DBTestSuite) BeforeTest(suiteName, testName string) {
	db, err := sql.Open("sqlite", fmt.Sprintf("file:%s?mode=memory&cache=shared", uuid.NewString()))
	if err != nil {
		logger.I.Panic("failed to open db", zap.Error(err))
	}
	driver, err := sqlite.WithInstance(db, &sqlite.Config{
		NoTxWrap: true,
	})
	if err != nil {
		logger.I.Panic("failed to attach db", zap.Error(err))
	}
	m, err := migrate.NewWithDatabaseInstance(
		"file://migrations",
		"sqlite",
		driver,
	)
	if err != nil {
		logger.I.Panic("failed to migrate db", zap.Error(err))
	}
	m.Up()
	suite.db = db
}

func (suite *DBTestSuite) AfterTest(suiteName, testName string) {
	_ = suite.db.Close()
}

func (suite *DBTestSuite) TestCreateManyStation() {
	tests := []struct {
		input         []*models.Station
		isError       bool
		errorContains []string
		expectedCount int64
		title         string
	}{
		{
			input: []*models.Station{
				fakeStation("id"),
				fakeStation("id2"),
			},
			expectedCount: 2,
			title:         "Positive test",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			ctx := context.Background()

			// Act
			err := db.CreateManyStation(ctx, suite.db, tt.input)

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				count, err := models.Stations().Count(ctx, suite.db)
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expectedCount, count)
			}
		})
	}
}

func (suite *DBTestSuite) TestFindOneStationAndFavorite() {
	// Arrange
	err := fakeStation("id").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id2").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id3").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	m := models.Favorite{
		StationID: "id2",
		UserID:    "userid",
	}
	m.Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	m = models.Favorite{
		StationID: "id3",
		UserID:    "otheruser",
	}
	m.Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)

	tests := []struct {
		input struct {
			id     string
			userID string
		}
		expected      *types.StationAndFavorite
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "id",
				userID: "userid",
			},
			expected: &types.StationAndFavorite{
				Station: *fakeStation("id"),
			},
			title: "Positive test: find one with no favorite",
		},
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "id2",
				userID: "userid",
			},
			expected: &types.StationAndFavorite{
				Station:  *fakeStation("id2"),
				Favorite: true,
			},
			title: "Positive test: find one with favorite by us",
		},
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "id3",
				userID: "userid",
			},
			expected: &types.StationAndFavorite{
				Station: *fakeStation("id3"),
			},
			title: "Positive test: find one with favorite by someone else",
		},
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "idnotexists",
				userID: "userid",
			},
			isError:       true,
			errorContains: []string{"sql: no rows in result set"},
			title:         "Negative test: find none",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			got, err := db.FindOneStationAndFavorite(ctx, suite.db, tt.input.id, tt.input.userID)

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, got)
			}
		})
	}
}

func (suite *DBTestSuite) TestFindManyFindManyStationAndFavorite() {
	// Arrange
	err := fakeStation("id").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id2").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id3").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	m := models.Favorite{
		StationID: "id2",
		UserID:    "userid",
	}
	m.Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	m = models.Favorite{
		StationID: "id3",
		UserID:    "otheruser",
	}
	m.Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)

	tests := []struct {
		input struct {
			userID string
			search string
			limit  int
			page   int
		}
		expected      []*types.StationAndFavorite
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: struct {
				userID string
				search string
				limit  int
				page   int
			}{
				userID: "userid",
				search: "",
				limit:  100,
				page:   1,
			},
			expected: []*types.StationAndFavorite{
				{
					Station: *fakeStation("id"),
				},
				{
					Station:  *fakeStation("id2"),
					Favorite: true,
				},
				{
					Station: *fakeStation("id3"),
				},
			},
			title: "Positive test: find all station",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			got, err := db.FindManyStationAndFavorite(ctx, suite.db, tt.input.userID, tt.input.search, tt.input.limit, tt.input.page)

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, got)
			}
		})
	}
}

func (suite *DBTestSuite) TestCreateFavorite() {
	// Arrange
	err := fakeStation("id").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id2").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id3").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)

	tests := []struct {
		input         models.Favorite
		expected      int64
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: models.Favorite{
				StationID: "id",
				UserID:    "userid",
			},
			expected: 1,
			title:    "Positive test: find one with no favorite",
		},
		{
			input: models.Favorite{
				StationID: "idnonexists",
				UserID:    "userid",
			},
			isError:       true,
			errorContains: []string{"FOREIGN KEY constraint failed"},
			title:         "Negative test: cannot create favorite on non existant station",
		},
		{
			input: models.Favorite{
				StationID: "idnonexists",
				UserID:    "",
			},
			isError:       true,
			errorContains: []string{"CHECK constraint failed: user_id <> ''"},
			title:         "Negative test: cannot create favorite on empty userID",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			err := db.CreateFavorite(ctx, suite.db, &tt.input)

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				count, err := models.Favorites().Count(ctx, suite.db)
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, count)
			}
		})
	}
}

func (suite *DBTestSuite) TestDeleteFavorite() {
	// Arrange
	err := fakeStation("id").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id2").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	err = fakeStation("id3").Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	m := models.Favorite{
		StationID: "id2",
		UserID:    "userid",
	}
	m.Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)
	m = models.Favorite{
		StationID: "id3",
		UserID:    "otheruser",
	}
	m.Insert(context.Background(), suite.db, boil.Infer())
	suite.Require().NoError(err)

	tests := []struct {
		input         models.Favorite
		expected      int64
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: models.Favorite{
				StationID: "idnonexists",
				UserID:    "userid",
			},
			expected: 2,
			title:    "Positive test: do nothing on unknown delete",
		},
		{
			input: models.Favorite{
				StationID: "id2",
				UserID:    "userid",
			},
			expected: 1,
			title:    "Positive test: delete one",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			err := db.DeleteFavorite(ctx, suite.db, &tt.input)

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				count, err := models.Favorites().Count(ctx, suite.db)
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, count)
			}
		})
	}
}

func TestDataSourceTestSuite(t *testing.T) {
	suite.Run(t, new(DBTestSuite))
}
