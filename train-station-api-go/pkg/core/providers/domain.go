package providers

import (
	firebaseAuth "firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/internal"
	dataStation "github.com/Darkness4/train-station-api/pkg/data/database/station"
	"github.com/Darkness4/train-station-api/pkg/domain/auth"
	"github.com/Darkness4/train-station-api/pkg/domain/station"
	"github.com/valyala/fasthttp"
)

func StationRepository(ds dataStation.DataSource, http *fasthttp.Client) station.Repository {
	internal.Logger.Debug("Provide StationRepository")
	r := dataStation.NewRepository(ds, http)
	go initStationRepository(r)
	return r
}

func AuthService(client *firebaseAuth.Client) auth.Service {
	internal.Logger.Debug("Provide AuthService")
	return auth.NewService(client)
}

func initStationRepository(stationRepo station.Repository) {
	if err := stationRepo.Preload(); err != nil {
		internal.Logger.Panic(err)
	}
}
