package presentation

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core/providers"
	"github.com/Darkness4/train-station-api/pkg/core/root/data"
	"github.com/Darkness4/train-station-api/pkg/core/root/domain"
	"github.com/savsgio/atreugo/v11"
)

type Module struct {
	Server *atreugo.Atreugo
}

func NewModule(data *data.Module, domain *domain.Module) *Module {
	internal.Logger.Debug("Instanciating PresentationModule")
	server := providers.HTTPServer(domain.StationRepository, domain.AuthService)
	return &Module{
		Server: server,
	}
}

func (m *Module) Dispose() {}
