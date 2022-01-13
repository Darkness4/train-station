package root

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/gofiber/fiber/v2"
)

type Controller struct {
	server *fiber.App
}

func NewController(server *fiber.App) *Controller {
	if server == nil {
		internal.Logger.Panic("NewController: server is nil")
	}
	ctrl := Controller{server}
	ctrl.buildRoutes()
	return &ctrl
}

func (ctrl *Controller) buildRoutes() {
	ctrl.server.Get("/", getRoot)
	ctrl.server.Get("/healthz", getHealthz)
	ctrl.server.Get("/openapi", getOpenApi)
}

func getRoot(ctx *fiber.Ctx) error {
	return ctx.Redirect("/api", fiber.StatusFound)
}

func getHealthz(ctx *fiber.Ctx) error {
	return ctx.JSON(&map[string]string{"status": "OK"})
}

func getOpenApi(ctx *fiber.Ctx) error {
	return ctx.SendFile("./static/openapi.yaml")
}
