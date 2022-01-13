package root

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/savsgio/atreugo/v11"
)

type Controller struct {
	server *atreugo.Atreugo
}

func NewController(server *atreugo.Atreugo) *Controller {
	if server == nil {
		internal.Logger.Panic("NewController: server is nil")
	}
	ctrl := Controller{server}
	ctrl.buildRoutes()
	return &ctrl
}

func (ctrl *Controller) buildRoutes() {
	ctrl.server.GET("/", getRoot)
	ctrl.server.GET("/healthz", getHealthz)
	ctrl.server.GET("/openapi", getOpenApi)
}

func getRoot(ctx *atreugo.RequestCtx) error {
	return ctx.RedirectResponse("/api", 302)
}

func getHealthz(ctx *atreugo.RequestCtx) error {
	return ctx.JSONResponse(&map[string]string{"status": "OK"}, 200)
}

func getOpenApi(ctx *atreugo.RequestCtx) error {
	ctx.SendFile("./static/openapi.yaml")
	return nil
}
