package station_test

import (
	"fmt"
	"testing"

	"github.com/Darkness4/train-station-api/pkg/data/database/fields"
	"github.com/Darkness4/train-station-api/pkg/data/database/geometry"
	"github.com/Darkness4/train-station-api/pkg/data/database/isfavorite"
	"github.com/Darkness4/train-station-api/pkg/data/database/station"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func TestNewDataSource(t *testing.T) {
	t.Run("NewDataSource should work when depencies are injected", func(t *testing.T) {
		database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
		if err != nil {
			t.Errorf("Received error when opening database: %v\n", err)
		}

		got := station.NewDataSource(database)
		if got == nil {
			t.Error("Got nil on NewDataSource")
		}
	})

	t.Run("NewDataSource shouldn't work when depencies are not injected", func(t *testing.T) {
		defer func() {
			if r := recover(); r != nil {
				t.Logf("Got expected internal.Logger.Panic: %v", r)
				return
			}
			t.Errorf("NewDataSource didn't internal.Logger.Panic")
		}()

		station.NewDataSource(nil)
	})
}

func TestCreateStation(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
	})

	// Tests
	t.Run("Create should work", func(t *testing.T) {
		in := station.Model{
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

		_, err := ds.CreateStation(&in)
		if err != nil {
			t.Errorf("Got err on Create: %v\n", err)
		}

		var count int64
		database.Model(&station.Model{}).Count(&count)
		if count != 1 {
			t.Errorf("Data wasn't stored: count %d\n", count)
		}
	})

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
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
	})

	// Tests
	t.Run("CreateMany should work", func(t *testing.T) {
		in := []*station.Model{
			{
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
		}

		_, err := ds.CreateManyStation(in)
		if err != nil {
			t.Errorf("Got err on CreateMany: %v\n", err)
		}

		var count int64
		database.Model(&station.Model{}).Count(&count)
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
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	mock := station.Model{
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
	}
	result = database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
		result := database.Create(&mock)
		if result.Error != nil {
			t.Errorf("Received error when resetting the database: %v\n", err)
		}
	})

	// Tests
	t.Run("Update should work", func(t *testing.T) {
		in := &station.Model{
			Libelle: "updated",
		}

		_, err := ds.UpdateStation("recordid", in)
		if err != nil {
			t.Errorf("Got err on Update: %v\n", err)
		}

		m := &station.Model{
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
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	mock := station.Model{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorites:     make([]*isfavorite.Model, 0),
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
	}
	result = database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
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
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	mock := station.Model{
		RecordID:        "recordid",
		DatasetID:       "datasetid",
		IsFavorites:     make([]*isfavorite.Model, 0),
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
	}
	result = database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	mockIsFavorite := isfavorite.Model{
		UserID:    "userId",
		StationID: mock.RecordID,
	}
	result = database.Create(&mockIsFavorite)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
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
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	mock := station.Model{
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
	}
	result = database.Create(&mock)
	if result.Error != nil {
		t.Errorf("Received error when mocking the database: %v\n", err)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
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
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
		database.Where("1 = 1").Delete(&isfavorite.Model{})
	})

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
		t.Run(fmt.Sprintf("%s should work", tt.name), func(t *testing.T) {
			t.Cleanup(func() {
				database.Where("1 = 1").Delete(&station.Model{})
				database.Where("1 = 1").Delete(&isfavorite.Model{})
			})

			_, err := ds.CreateStation(tt.mock)
			if err != nil {
				t.Errorf("Got err on CreateStation: %v\n", err)
				t.FailNow()
			}

			got, err := ds.CreateIsFavorite(tt.in)
			if err != nil {
				t.Errorf("Got err on CreateIsFavorite: %v\n", err)
				t.FailNow()
			}

			if got.StationID != tt.mock.RecordID {
				t.Errorf("got.StationID != newModel.RecordID: got %v\n", got)
				t.FailNow()
			}

			var count int64
			database.Model(&isfavorite.Model{}).Count(&count)
			if count != 1 {
				t.Errorf("Data wasn't stored: count %d\n", count)
				t.FailNow()
			}

			testModel := station.Model{
				RecordID: "recordid",
			}
			database.Preload("IsFavorites").First(&testModel)
			if len(testModel.IsFavorites) != 1 && (*testModel.IsFavorites[0]).UserID == "userId" {
				t.Errorf("Relation with IsFavorites between station not found: station %v\n", testModel)
				t.FailNow()
			}

			if (*testModel.IsFavorites[0]).UserID != "userId" {
				t.Errorf("Relation with IsFavorites between station doesn't correspond: testModel.IsFavorites[0] %v\n", (*testModel.IsFavorites[0]))
				t.FailNow()
			}
		})
	}

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
		t.Run(fmt.Sprintf("%s should not work", tt.name), func(t *testing.T) {
			got, err := ds.CreateIsFavorite(tt.in)
			if err != nil {
				t.Logf("Got expected err on CreateIsFavorite: %v\n", err)
				return
			}
			t.Errorf("Continuation error. Eror was expected. Got: %v\n", got)
		})
	}
}

func TestRemoveIsFavorite(t *testing.T) {
	// Setup
	database, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		t.Errorf("Received error when opening database: %v\n", err)
	}
	err = database.AutoMigrate(
		&station.Model{},
		&isfavorite.Model{},
	)
	if err != nil {
		t.Errorf("Received error when migrating database: %v\n", err)
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		t.Errorf("Received error when PRAGMA foreign_keys = ON: %v\n", result.Error)
	}
	ds := station.NewDataSource(database)
	t.Cleanup(func() {
		database.Where("1 = 1").Delete(&station.Model{})
		database.Where("1 = 1").Delete(&isfavorite.Model{})
	})

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
		t.Run(fmt.Sprintf("%s should work", tt.name), func(t *testing.T) {
			t.Cleanup(func() {
				database.Where("1 = 1").Delete(&station.Model{})
				database.Where("1 = 1").Delete(&isfavorite.Model{})
			})

			_, err := ds.CreateStation(tt.mock)
			if err != nil {
				t.Errorf("Got err on CreateStation: %v\n", err)
				return
			}

			_, err = ds.CreateIsFavorite(tt.in)
			if err != nil {
				t.Errorf("Got err on CreateIsFavorite: %v\n", err)
				return
			}

			err = ds.RemoveIsFavorite(tt.in)
			if err != nil {
				t.Errorf("Got err on RemoveIsFavorite: %v\n", err)
				return
			}

			var count int64
			if database.Model(&isfavorite.Model{}).Count(&count); count != 0 {
				t.Error("Entity not deleted")
				return
			}
		})
	}
}
