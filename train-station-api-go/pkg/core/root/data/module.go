package data

import (
	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core/providers"
	"github.com/Darkness4/train-station-api/pkg/data/database/station"
	"github.com/valyala/fasthttp"
	"gorm.io/gorm"
)

type Module struct {
	AuthClient        *auth.Client
	db                *gorm.DB
	firebaseApp       *firebase.App
	HTTPClient        *fasthttp.Client
	StationDataSource station.DataSource
}

func NewModule() *Module {
	internal.Logger.Debug("Instanciating DataModule")
	tls := providers.TLSConfig()
	http := providers.HTTP(tls)
	firebaseApp := providers.FirebaseApp(http)
	authClient := providers.AuthClient(firebaseApp)
	db := providers.DB()
	fastHTTP := providers.FastHTTP(tls)
	stationDS := providers.StationDataSource(db)

	return &Module{
		AuthClient:        authClient,
		db:                db,
		firebaseApp:       firebaseApp,
		HTTPClient:        fastHTTP,
		StationDataSource: stationDS,
	}
}

func (m *Module) Dispose() {
	if db, _ := m.db.DB(); db != nil {
		db.Close()
	}
}
