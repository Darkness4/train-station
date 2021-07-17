package ds

import (
	"fmt"
	"testing"

	"github.com/Darkness4/train-station-api/pkg/data/models"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func TestNewStationDataSource(t *testing.T) {
	t.Run("NewStationDataSource should work when depencies are injected", func(t *testing.T) {
		database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
		if err != nil {
			t.Errorf("Received error when opening database: %v\n", err)
		}

		got := NewStationDataSource(database)
		if got == nil {
			t.Error("Got nil on NewStationDataSource")
		}
	})

	t.Run("NewStationDataSource shouldn't work when depencies are not injected", func(t *testing.T) {
		defer func() {
			if r := recover(); r != nil {
				t.Logf("Got expected panic: %v", r)
				return
			}
			t.Errorf("NewStationDataSource didn't panic")
		}()

		NewStationDataSource(nil)
	})
}

func TestCreateStation(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	ds := NewStationDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&models.StationModel{})
	})

	// Tests
	t.Run("Create should work", func(t *testing.T) {
		in := models.StationModel{
			RecordID:        "recordid",
			DatasetID:       "datasetid",
			Libelle:         "libelle",
			IsFavorites:     make([]*models.IsFavoriteModel, 0),
			RecordTimestamp: "record_timestamp",
			Fields: &models.FieldsModel{
				GeoPoint2D: "[]",
				CGeo:       "[]",
				GeoShape: &models.GeometryModel{
					Coordinates: "[]",
				},
			},
			Geometry: &models.GeometryModel{
				Coordinates: "[]",
			},
		}

		_, err := ds.CreateStation(&in)
		if err != nil {
			t.Errorf("Got err on Create: %v\n", err)
		}

		var count int64
		database.Model(&models.StationModel{}).Count(&count)
		if count != 1 {
			t.Errorf("Data wasn't stored: count %d\n", count)
		}
	})

	var nonWorkingTable = []struct {
		name string
		in   *models.StationModel
	}{
		{"Empty Object", &models.StationModel{}},
		{"First layer filled", &models.StationModel{
			RecordID:        "recordid",
			DatasetID:       "datasetid",
			Libelle:         "libelle",
			RecordTimestamp: "record_timestamp",
			Fields:          &models.FieldsModel{},
			Geometry:        &models.GeometryModel{},
		}},
	}

	for _, tt := range nonWorkingTable {
		t.Run(fmt.Sprintf("%s should not work", tt.name), func(t *testing.T) {
			_, err := ds.CreateStation(tt.in)
			if err != nil {
				t.Logf("Got expected err on Create: %v\n", err)
				return
			}
			t.Error("Continuation error. Eror was expected.")
		})
	}
}

func TestCreateManyStation(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	ds := NewStationDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&models.StationModel{})
	})

	// Tests
	t.Run("CreateMany should work", func(t *testing.T) {
		in := []*models.StationModel{
			{
				RecordID:        "recordid",
				DatasetID:       "datasetid",
				Libelle:         "libelle",
				RecordTimestamp: "record_timestamp",
				Fields: &models.FieldsModel{
					GeoPoint2D: "[]",
					CGeo:       "[]",
					GeoShape: &models.GeometryModel{
						Coordinates: "[]",
					},
				},
				Geometry: &models.GeometryModel{
					Coordinates: "[]",
				},
			},
		}

		_, err := ds.CreateManyStation(in)
		if err != nil {
			t.Errorf("Got err on CreateMany: %v\n", err)
		}

		var count int64
		database.Model(&models.StationModel{}).Count(&count)
		if count != 1 {
			t.Errorf("Data wasn't stored: count %d\n", count)
		}
	})
}

func TestUpdateStation(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		Libelle:         "libelle",
		RecordTimestamp: "record_timestamp",
		Fields: &models.FieldsModel{
			GeoPoint2D: "[]",
			CGeo:       "[]",
			GeoShape: &models.GeometryModel{
				Coordinates: "[]",
			},
		},
		Geometry: &models.GeometryModel{
			Coordinates: "[]",
		},
	}
	result := database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := NewStationDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&models.StationModel{})
		result := database.Create(&mock)
		if result.Error != nil {
			t.Errorf("Received error when resetting the database: %v\n", err)
		}
	})

	// Tests
	t.Run("Update should work", func(t *testing.T) {
		in := &models.StationModel{
			Libelle: "updated",
		}

		_, err := ds.UpdateStation("recordid", in)
		if err != nil {
			t.Errorf("Got err on Update: %v\n", err)
		}

		m := &models.StationModel{
			RecordID: "recordid",
		}
		result := database.First(m)
		if result.Error != nil {
			t.Errorf("Got error when fetching: %v\n", result.Error)
		}

		if m.Libelle != in.Libelle {
			t.Errorf("got != want. Got %v, want: %v\n", m.Libelle, in.Libelle)
		}
	})
}

func TestFindOneStation(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorites:     make([]*models.IsFavoriteModel, 0),
		Libelle:         "libelle",
		RecordTimestamp: "record_timestamp",
		Fields: &models.FieldsModel{
			GeoPoint2D: "[]",
			CGeo:       "[]",
			GeoShape: &models.GeometryModel{
				Coordinates: "[]",
			},
		},
		Geometry: &models.GeometryModel{
			Coordinates: "[]",
		},
	}
	result := database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := NewStationDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&models.StationModel{})
		result := database.Create(&mock)
		if result.Error != nil {
			t.Errorf("Received error when resetting the database: %v\n", err)
		}
	})

	// Tests
	t.Run("FindOne should work", func(t *testing.T) {
		got, err := ds.FindOneStation("recordid")
		if err != nil {
			t.Errorf("Got err on FindOne: %v\n", err)
		}

		if got == nil {
			t.Error("Got nil when fetching")
		}
	})
}

func TestFindManyAndCountStation(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorites:     make([]*models.IsFavoriteModel, 0),
		Libelle:         "libelle",
		RecordTimestamp: "record_timestamp",
		Fields: &models.FieldsModel{
			GeoPoint2D: "[]",
			CGeo:       "[]",
			GeoShape: &models.GeometryModel{
				Coordinates: "[]",
			},
		},
		Geometry: &models.GeometryModel{
			Coordinates: "[]",
		},
	}
	result := database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	mockIsFavorite := models.IsFavoriteModel{
		UserID:  "userId",
		Station: &mock,
	}
	result = database.Create(&mockIsFavorite)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := NewStationDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&models.StationModel{})
		result := database.Create(&mock)
		if result.Error != nil {
			t.Errorf("Received error when resetting the database: %v\n", err)
		}
	})

	// Tests
	t.Run("FindManyAndCount should work", func(t *testing.T) {
		got, count, err := ds.FindManyAndCountStation("", 0, 0)
		if err != nil {
			t.Errorf("Got err on FindManyAndCount: %v\n", err)
		}

		if got == nil {
			t.Error("Got nil when fetching")
		}

		if count != 1 || len(got) != 1 {
			t.Errorf("Count != 1 when fetching: count: %v\n", count)
		}

		if len(got[0].IsFavorites) != 1 {
			t.Errorf("Relation with IsFavorites not found: got: %v\n", got[0])
		}

		if got[0].IsFavorites[0].UserID != "userId" {
			t.Errorf("Relation with IsFavorites doesn't correspond: got: %v\n", got[0].IsFavorites[0])
		}
	})
}

func TestCountStation(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		Libelle:         "libelle",
		RecordTimestamp: "record_timestamp",
		Fields: &models.FieldsModel{
			GeoPoint2D: "[]",
			CGeo:       "[]",
			GeoShape: &models.GeometryModel{
				Coordinates: "[]",
			},
		},
		Geometry: &models.GeometryModel{
			Coordinates: "[]",
		},
	}
	result := database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := NewStationDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&models.StationModel{})
		result := database.Create(&mock)
		if result.Error != nil {
			t.Errorf("Received error when resetting the database: %v\n", err)
		}
	})

	// Tests
	t.Run("Count should work", func(t *testing.T) {
		count, err := ds.CountStation("")
		if err != nil {
			t.Errorf("Got err on Count: %v\n", err)
		}

		if count != 1 {
			t.Errorf("Count != 1 when fetching: count: %v\n", count)
		}
	})

	t.Run("Count should work with cont filter", func(t *testing.T) {
		count, err := ds.CountStation("libelle")
		if err != nil {
			t.Errorf("Got err on Count: %v\n", err)
		}

		if count != 1 {
			t.Errorf("Count != 1 when fetching: count: %v\n", count)
		}
	})

	t.Run("Count should work with not cont filter", func(t *testing.T) {
		count, err := ds.CountStation("notlib")
		if err != nil {
			t.Errorf("Got err on Count: %v\n", err)
		}

		if count != 0 {
			t.Errorf("Count != 0 when fetching: count: %v\n", count)
		}
	})
}

func TestCreateIsFavorite(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	ds := NewStationDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&models.StationModel{})
		database.Where("1 = 1").Delete(&models.IsFavoriteModel{})
	})

	// Tests
	t.Run("CreateIsFavorite should work", func(t *testing.T) {
		station := models.StationModel{
			RecordID:        "recordid",
			DatasetID:       "datasetid",
			Libelle:         "libelle",
			RecordTimestamp: "record_timestamp",
			Fields: &models.FieldsModel{
				GeoPoint2D: "[]",
				CGeo:       "[]",
				GeoShape: &models.GeometryModel{
					Coordinates: "[]",
				},
			},
			Geometry: &models.GeometryModel{
				Coordinates: "[]",
			},
		}

		newModel, err := ds.CreateStation(&station)
		if err != nil {
			t.Errorf("Got err on CreateStation: %v\n", err)
		}

		in := models.IsFavoriteModel{
			UserID:  "userId",
			Station: newModel,
		}

		_, err = ds.CreateIsFavorite(&in)
		if err != nil {
			t.Errorf("Got err on CreateIsFavorite: %v\n", err)
		}

		var count int64
		database.Model(&models.IsFavoriteModel{}).Count(&count)
		if count != 1 {
			t.Errorf("Data wasn't stored: count %d\n", count)
		}

		testModel := models.StationModel{
			RecordID: "recordid",
		}
		database.Preload("IsFavorites").First(&testModel)
		if len(testModel.IsFavorites) != 1 && (*testModel.IsFavorites[0]).UserID == "userId" {
			t.Errorf("Relation with IsFavorites between station not found: station %v\n", testModel)
		}

		if (*testModel.IsFavorites[0]).UserID != "userId" {
			t.Errorf("Relation with IsFavorites between station doesn't correspond: testModel.IsFavorites[0] %v\n", (*testModel.IsFavorites[0]))
		}
	})

	var nonWorkingTable = []struct {
		name string
		in   *models.IsFavoriteModel
	}{
		{"Empty Object", &models.IsFavoriteModel{}},
		{"Non existing relation", &models.IsFavoriteModel{
			UserID:  "fakeid",
			Station: &models.StationModel{},
		}},
	}

	for _, tt := range nonWorkingTable {
		t.Run(fmt.Sprintf("%s should not work", tt.name), func(t *testing.T) {
			_, err := ds.CreateIsFavorite(tt.in)
			if err != nil {
				t.Logf("Got expected err on CreateIsFavorite: %v\n", err)
				return
			}
			t.Error("Continuation error. Eror was expected.")
		})
	}
}
