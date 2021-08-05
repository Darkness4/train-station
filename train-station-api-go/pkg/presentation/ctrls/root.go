package ctrls

import "github.com/savsgio/atreugo/v11"

type RootController struct {
	server *atreugo.Atreugo
}

func NewRootController(server *atreugo.Atreugo) *RootController {
	if server == nil {
		panic("NewRootController: server is nil")
	}
	ctrl := RootController{server}
	ctrl.buildRoutes()
	return &ctrl
}

func (ctrl *RootController) buildRoutes() {
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
