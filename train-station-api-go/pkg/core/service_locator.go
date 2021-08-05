package core

import (
	"context"
	"encoding/json"
	"log"
	"os"

	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/pkg/data/ds"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	repoimpl "github.com/Darkness4/train-station-api/pkg/data/repos"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/Darkness4/train-station-api/pkg/domain/repos"
	"github.com/Darkness4/train-station-api/pkg/domain/services"
	"github.com/spf13/viper"
	"github.com/valyala/fasthttp"
	"google.golang.org/api/option"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

type ServiceLocator struct {
	// Firebase
	FirebaseApp *firebase.App
	AuthClient  *auth.Client

	// Data
	DB                *gorm.DB
	StationDataSource ds.StationDataSource

	// Domain
	StationRepository repos.StationRepository
	AuthService       services.AuthService
}

func NewServiceLocator() (*ServiceLocator, error) {
	// Firebase
	creds := viper.GetString("GOOGLE_APPLICATION_CREDENTIALS")
	opt := option.WithCredentialsFile(creds)
	app, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		return nil, err
	}
	client, err := app.Auth(context.Background())
	if err != nil {
		return nil, err
	}

	// Data
	err = os.Remove("cache.sqlite3")
	if err != nil && !os.IsNotExist(err) {
		return nil, err
	}
	database, err := gorm.Open(sqlite.Open("cache.sqlite3"), &gorm.Config{})
	if err != nil {
		return nil, err
	}
	err = database.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		return nil, err
	}
	result := database.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		return nil, result.Error
	}

	stationDS := ds.NewStationDataSource(database)

	// Domain
	stationRepo := repoimpl.NewStationRepository(stationDS)
	go initializeDatabase(stationRepo)

	authService := services.NewAuthService(client)

	// Output
	return &ServiceLocator{
		FirebaseApp:       app,
		AuthClient:        client,
		DB:                database,
		StationDataSource: stationDS,
		StationRepository: stationRepo,
		AuthService:       authService,
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
	var data []*entities.Station
	json.Unmarshal(body, &data)

	result, err := stationRepo.CreateMany(data, "0")
	if err != nil {
		log.Printf("InitializeDatabase: Error %s\n", err.Error())
		return
	}

	log.Printf("InitializeDatabase: Changed lines %d\n", len(result))
}
