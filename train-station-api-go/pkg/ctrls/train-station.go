package ctrls

import (
	"encoding/json"

	"github.com/Darkness4/train-station-api/pkg/ctrls/dtos"
	"github.com/Darkness4/train-station-api/pkg/domain/svcs"
	"github.com/savsgio/atreugo/v11"
)

type TrainStationController struct {
	api             *atreugo.Router
	trainStationSvc *svcs.TrainStationService
}

func NewTrainStationController(
	api *atreugo.Router,
	trainStationService *svcs.TrainStationService,
) *TrainStationController {
	if api == nil {
		panic("NewTrainStationController: api is nil")
	}
	if api == nil {
		panic("NewTrainStationController: trainStationSvc is nil")
	}
	ctrl := TrainStationController{api, trainStationService}
	ctrl.buildRoutes()
	return &ctrl
}

func (ctrl *TrainStationController) buildRoutes() {
	stations := ctrl.api.NewGroupPath("/stations")
	stations.GET("/", func(ctx *atreugo.RequestCtx) error {
		return ctrl.getManyStations(ctx)
	})
	stations.GET("/{id}", func(ctx *atreugo.RequestCtx) error {
		return ctrl.getOneStation(ctx)
	})
	stations.PATCH("/{id}", func(ctx *atreugo.RequestCtx) error {
		return ctrl.updateOneStation(ctx)
	})
	stations.POST("/", func(ctx *atreugo.RequestCtx) error {
		return ctrl.createOneStation(ctx)
	})
}

func (ctrl *TrainStationController) getManyStations(ctx *atreugo.RequestCtx) error {
	// Process
	stations, err := ctrl.trainStationSvc.GetManyStations()
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Output
	return ctx.JSONResponse(stations, 200)
}

func (ctrl *TrainStationController) getOneStation(ctx *atreugo.RequestCtx) error {
	// Input
	id := ctx.UserValue("id").(string)

	// Process
	station, err := ctrl.trainStationSvc.GetOneStation(id)
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Output
	return ctx.JSONResponse(station, 200)
}

func (ctrl *TrainStationController) createOneStation(ctx *atreugo.RequestCtx) error {
	// Input
	dto := dtos.CreateStation{}
	json.Unmarshal(ctx.PostBody(), &dto)

	// Process
	station, err := dto.Entity()
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}
	newStation, err := ctrl.trainStationSvc.CreateOneStation(*station)
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Output
	return ctx.JSONResponse(newStation, 201)
}

func (ctrl *TrainStationController) updateOneStation(ctx *atreugo.RequestCtx) error {
	// Input
	dto := dtos.UpdateStation{}
	json.Unmarshal(ctx.PostBody(), &dto)
	id := ctx.UserValue("id").(string)

	// Process
	options, err := dto.Entity()
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}
	newStation, err := ctrl.trainStationSvc.UpdateOneStation(id, *options)
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Output
	return ctx.JSONResponse(newStation, 201)
}
