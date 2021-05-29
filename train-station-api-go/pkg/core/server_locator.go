package core

import (
	"github.com/Darkness4/train-station-api/pkg/data/db"
	"github.com/Darkness4/train-station-api/pkg/domain/svcs"
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
	stationRepository := db.NewStationRepository(database)

	// Domain
	trainStationService := svcs.NewTrainStationService(stationRepository)

	// Output
	return &ServiceLocator{
		DB:                  database,
		StationRepository:   stationRepository,
		TrainStationService: trainStationService,
	}, nil
}
