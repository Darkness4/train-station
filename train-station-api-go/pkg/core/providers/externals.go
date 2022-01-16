package providers

import (
	"context"
	"crypto/tls"
	"encoding/base64"

	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/data/database"
	"github.com/Darkness4/train-station-api/pkg/data/database/isfavorite"
	"github.com/Darkness4/train-station-api/pkg/data/database/station"
	"github.com/certifi/gocertifi"
	"github.com/glebarez/sqlite"
	"github.com/spf13/viper"
	"github.com/valyala/fasthttp"
	"google.golang.org/api/option"
	"gorm.io/gorm"
)

func FirebaseApp() *firebase.App {
	internal.Logger.Debug("Provide FirebaseApp")
	creds := viper.GetString("GOOGLE_APPLICATION_CREDENTIALS")
	credsOpt := option.WithCredentialsFile(creds)
	app, err := firebase.NewApp(context.Background(), nil, credsOpt)
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
	db, err := gorm.Open(sqlite.Open("cache.sqlite3"), &gorm.Config{
		Logger: &database.GormLogger{
			SkipErrRecordNotFound: true,
		},
	})
	if err != nil {
		internal.Logger.Panic(err)
	}
	if err := db.AutoMigrate(
		&station.Model{},
		&isfavorite.Model{},
	); err != nil {
		internal.Logger.Panic(err)
	}
	return db
}

func TLSConfig() *tls.Config {
	internal.Logger.Debug("Provide TLSConfig")
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
	return &tls.Config{
		RootCAs:            certPool,
		InsecureSkipVerify: false,
		MinVersion:         tls.VersionTLS12,
	}
}

func HTTP(tls *tls.Config) *fasthttp.Client {
	internal.Logger.Debug("Provide FastHTTP")

	return &fasthttp.Client{
		TLSConfig: tls,
	}
}
