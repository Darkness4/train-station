package sl

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

var (
	FirebaseApp *firebase.App
	AuthClient  *auth.Client

	DB                *gorm.DB
	StationDataSource ds.StationDataSource

	StationRepository repos.StationRepository
	AuthService       services.AuthService
)

func init() {
	// Firebase
	creds := viper.GetString("GOOGLE_APPLICATION_CREDENTIALS")
	opt := option.WithCredentialsFile(creds)
	FirebaseApp, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		panic(err)
	}
	AuthClient, err = FirebaseApp.Auth(context.Background())
	if err != nil {
		panic(err)
	}

	// Data
	err = os.Remove("cache.sqlite3")
	if err != nil && !os.IsNotExist(err) {
		panic(err)
	}
	DB, err = gorm.Open(sqlite.Open("cache.sqlite3"), &gorm.Config{})
	if err != nil {
		panic(err)
	}
	err = DB.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	)
	if err != nil {
		panic(err)
	}
	result := DB.Exec("PRAGMA foreign_keys = ON")
	if result.Error != nil {
		panic(result.Error)
	}

	StationDataSource = ds.NewStationDataSource(DB)

	// Domain
	StationRepository = repoimpl.NewStationRepository(StationDataSource)
	go initializeDatabase(StationRepository)

	AuthService = services.NewAuthService(AuthClient)
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
