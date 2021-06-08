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

func TestCreate(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
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
		isFavorite := false
		in := models.StationModel{
			RecordID:        "recordid",
			DatasetID:       "datasetid",
			IsFavorite:      &isFavorite,
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

		_, err := ds.Create(&in)
		if err != nil {
			t.Errorf("Got err on Create: %v\n", err)
		}

		var count int64
		database.Model(&models.StationModel{}).Count(&count)
		if count != 1 {
			t.Errorf("Data wasn't stored: count %d\n", count)
		}
	})

	var isFavorite bool
	var nonWorkingTable = []struct {
		name string
		in   *models.StationModel
	}{
		{"Empty Object", &models.StationModel{}},
		{"First layer filled", &models.StationModel{
			RecordID:        "recordid",
			DatasetID:       "datasetid",
			IsFavorite:      &isFavorite,
			Libelle:         "libelle",
			RecordTimestamp: "record_timestamp",
			Fields:          &models.FieldsModel{},
			Geometry:        &models.GeometryModel{},
		}},
	}

	for _, tt := range nonWorkingTable {
		t.Run(fmt.Sprintf("%s should not work", tt.name), func(t *testing.T) {
			_, err := ds.Create(tt.in)
			if err != nil {
				t.Logf("Got expected err on Create: %v\n", err)
				return
			}
			t.Error("Continuation error. Eror was expected.")
		})
	}
}

func TestCreateMany(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
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
		isFavorite := false
		in := []*models.StationModel{
			{
				RecordID:        "recordid",
				DatasetID:       "datasetid",
				IsFavorite:      &isFavorite,
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

		_, err := ds.CreateMany(in)
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

func TestUpdate(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	isFavorite := false
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorite:      &isFavorite,
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
		want := true
		in := &models.StationModel{
			IsFavorite: &want,
		}

		_, err := ds.Update("recordid", in)
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

		if *m.IsFavorite != want {
			t.Errorf("got != want. Got %v, want: %v\n", *m.IsFavorite, want)
		}
	})
}

func TestFindOne(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	isFavorite := false
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorite:      &isFavorite,
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
		got, err := ds.FindOne("recordid")
		if err != nil {
			t.Errorf("Got err on FindOne: %v\n", err)
		}

		if got == nil {
			t.Error("Got nil when fetching")
		}
	})
}

func TestFindManyAndCount(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	isFavorite := false
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorite:      &isFavorite,
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
	t.Run("FindManyAndCount should work", func(t *testing.T) {
		got, count, err := ds.FindManyAndCount("", 0, 0)
		if err != nil {
			t.Errorf("Got err on FindManyAndCount: %v\n", err)
		}

		if got == nil {
			t.Error("Got nil when fetching")
		}

		if count != 1 {
			t.Errorf("Count != 1 when fetching: count: %v\n", count)
		}
	})
}

func TestCount(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&models.StationModel{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	isFavorite := false
	mock := models.StationModel{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorite:      &isFavorite,
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
		count, err := ds.Count()
		if err != nil {
			t.Errorf("Got err on Count: %v\n", err)
		}

		if count != 1 {
			t.Errorf("Count != 1 when fetching: count: %v\n", count)
		}
	})
}
