package presentation

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core/providers"
	"github.com/Darkness4/train-station-api/pkg/core/root/data"
	"github.com/Darkness4/train-station-api/pkg/core/root/domain"
	"github.com/gofiber/fiber/v2"
)

type Module struct {
	Server *fiber.App
}

func NewModule(data *data.Module, domain *domain.Module) *Module {
	internal.Logger.Debug("Instanciating PresentationModule")
	server := providers.HTTPServer(domain.StationRepository, domain.AuthService)
	return &Module{
		Server: server,
	}
}

func (m *Module) Dispose() {}
