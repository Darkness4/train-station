package domain

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core/providers"
	"github.com/Darkness4/train-station-api/pkg/core/root/data"
	"github.com/Darkness4/train-station-api/pkg/domain/auth"
	"github.com/Darkness4/train-station-api/pkg/domain/station"
)

type Module struct {
	AuthService       auth.Service
	StationRepository station.Repository
}

func NewModule(data *data.Module) *Module {
	internal.Logger.Debug("Instanciating DomainModule")
	stations := providers.StationRepository(data.StationDataSource, data.HTTPClient)
	auth := providers.AuthService(data.AuthClient)

	return &Module{
		AuthService:       auth,
		StationRepository: stations,
	}
}

func (m *Module) Dispose() {
}
