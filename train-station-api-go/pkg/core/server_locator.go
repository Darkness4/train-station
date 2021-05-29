package core

import (
	"encoding/json"
	"log"
	"os"

	"github.com/Darkness4/train-station-api/pkg/data/db"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	repoimpl "github.com/Darkness4/train-station-api/pkg/data/repos"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/Darkness4/train-station-api/pkg/domain/repos"
	"github.com/valyala/fasthttp"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

type ServiceLocator struct {
	// Data
	DB                *gorm.DB
	StationDataSource db.StationDataSource

	// Domain
	StationRepository repos.StationRepository
}

func NewServiceLocator() (*ServiceLocator, error) {
	// Data
	err := os.Remove("cache.sqlite3")
	if err != nil && !os.IsNotExist(err) {
		return nil, err
	}
	database, err := gorm.Open(sqlite.Open("cache.sqlite3"), &gorm.Config{})
	if err != nil {
		return nil, err
	}
	database.AutoMigrate(
		&models.StationModel{},
	)

	stationDS := db.NewStationDataSource(database)

	// Domain
	stationRepo := repoimpl.NewStationRepository(stationDS)
	go initializeDatabase(stationRepo)

	// Output
	return &ServiceLocator{
		DB:                database,
		StationDataSource: stationDS,
		StationRepository: stationRepo,
	}, nil
}

func initializeDatabase(stationRepo repos.StationRepository) {
	statusCode, body, err := fasthttp.Get(nil, "https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json")
	if statusCode != 200 {
		log.Printf("InitializeDatabase: Status code wasn't 200, it was %d\n", statusCode)
		return
	}
	if err != nil {
		log.Printf("InitializeDatabase: Error %s\n", err.Error())
		return
	}
	var data []entities.Station
	json.Unmarshal(body, &data)

	result, err := stationRepo.CreateMany(data)
	if err != nil {
		log.Printf("InitializeDatabase: Error %s\n", err.Error())
		return
	}

	log.Printf("InitializeDatabase: Changed lines %d\n", len(result))
}
