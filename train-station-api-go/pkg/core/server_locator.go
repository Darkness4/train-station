package core

import (
	"encoding/json"
	"log"

	"github.com/Darkness4/train-station-api/pkg/data/db"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/Darkness4/train-station-api/pkg/domain/svcs"
	"github.com/valyala/fasthttp"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

type ServiceLocator struct {
	// Data
	DB                *gorm.DB
	StationRepository *db.StationRepository

	// Domain
	TrainStationService *svcs.TrainStationService
}

func NewServiceLocator() (*ServiceLocator, error) {
	// Data
	database, err := gorm.Open(sqlite.Open("cache.sqlite3"), &gorm.Config{})
	if err != nil {
		return nil, err
	}
	database.AutoMigrate(
		&models.StationModel{},
	)

	stationRepository := db.NewStationRepository(database)

	// Domain
	trainStationService := svcs.NewTrainStationService(stationRepository)
	go initializeDatabase(trainStationService)

	// Output
	return &ServiceLocator{
		DB:                  database,
		StationRepository:   stationRepository,
		TrainStationService: trainStationService,
	}, nil
}

func initializeDatabase(trainStationService *svcs.TrainStationService) {
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

	result, err := trainStationService.CreateMany(data)
	if err != nil {
		log.Printf("InitializeDatabase: Error %s\n", err.Error())
		return
	}

	log.Printf("InitializeDatabase: Changed lines %d\n", len(result))
}
