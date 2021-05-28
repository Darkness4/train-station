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
}

func getRoot(ctx *atreugo.RequestCtx) error {
	return ctx.RedirectResponse("/api", 302)
}
