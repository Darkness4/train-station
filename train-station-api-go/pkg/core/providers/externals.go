package providers

import (
	"context"
	"crypto/tls"
	"encoding/base64"
	"os"

	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/data/database"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/certifi/gocertifi"
	"github.com/spf13/viper"
	"github.com/valyala/fasthttp"
	"google.golang.org/api/option"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func FirebaseApp() *firebase.App {
	internal.Logger.Debug("Provide FirebaseApp")
	creds := viper.GetString("GOOGLE_APPLICATION_CREDENTIALS")
	opt := option.WithCredentialsFile(creds)
	app, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		internal.Logger.Panic(err)
	}
	return app
}

func AuthClient(firebaseApp *firebase.App) *auth.Client {
	internal.Logger.Debug("Provide AuthClient")
	client, err := firebaseApp.Auth(context.Background())
	if err != nil {
		internal.Logger.Panic(err)
	}
	return client
}

func DB() *gorm.DB {
	internal.Logger.Debug("Provide DB")
	err := os.Remove("cache.sqlite3")
	if err != nil && !os.IsNotExist(err) {
		internal.Logger.Panic(err)
	}
	db, err := gorm.Open(sqlite.Open("cache.sqlite3"), &gorm.Config{
		Logger: &database.GormLogger{
			SkipErrRecordNotFound: true,
		},
	})
	if err != nil {
		internal.Logger.Panic(err)
	}
	if err := db.AutoMigrate(
		&models.StationModel{},
		&models.IsFavoriteModel{},
	); err != nil {
		internal.Logger.Panic(err)
	}
	return db
}

func HTTP() *fasthttp.Client {
	internal.Logger.Debug("Provide HTTP")
	certPool, err := gocertifi.CACerts()
	if err != nil {
		internal.Logger.Panic(err.Error())
	}
	if viper.GetString("CA") != "" {
		if pem, err := base64.StdEncoding.DecodeString(viper.GetString("CA")); err == nil {
			certPool.AppendCertsFromPEM(pem)
		} else {
			internal.Logger.Warn("the certificate couldn't be decoded")
		}
	}
	return &fasthttp.Client{
		TLSConfig: &tls.Config{
			RootCAs: certPool,
		},
	}
}
