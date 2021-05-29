package ctrls

import (
	"encoding/json"
	"strconv"

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
		return ctrl.getMany(ctx)
	})
	stations.GET("/{id}", func(ctx *atreugo.RequestCtx) error {
		return ctrl.getOne(ctx)
	})
	stations.PATCH("/{id}", func(ctx *atreugo.RequestCtx) error {
		return ctrl.updateOne(ctx)
	})
	stations.POST("/", func(ctx *atreugo.RequestCtx) error {
		return ctrl.createOne(ctx)
	})
}

func (ctrl *TrainStationController) getMany(ctx *atreugo.RequestCtx) error {
	// Input
	args := ctx.QueryArgs()
	s := string(args.Peek("s"))

	limitStr := string(args.Peek("limit"))
	limit := 10
	if limitStr != "" && limitStr != "0" {
		newLimit, err := strconv.Atoi(limitStr)
		if err != nil {
			return ctx.JSONResponse(dtos.Error{
				StatusCode: 401,
				Message:    err.Error(),
			}, 401)
		}
		limit = newLimit
	}
	pageStr := string(args.Peek("page"))
	page := 1
	if pageStr != "" {
		newPage, err := strconv.Atoi(pageStr)
		if err != nil {
			return ctx.JSONResponse(dtos.Error{
				StatusCode: 401,
				Message:    err.Error(),
			}, 401)
		}
		page = newPage
	}

	// Process
	stations, count, err := ctrl.trainStationSvc.GetManyAndCount(s, limit, page)
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}
	total, err := ctrl.trainStationSvc.Total()
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}
	pageCount := total/int64(limit) + 1

	// Output
	response := dtos.PaginatedStation{
		Data:      stations,
		Count:     count,
		Total:     total,
		Page:      page,
		PageCount: pageCount,
	}
	return ctx.JSONResponse(response, 200)
}

func (ctrl *TrainStationController) getOne(ctx *atreugo.RequestCtx) error {
	// Input
	id := ctx.UserValue("id").(string)

	// Process
	station, err := ctrl.trainStationSvc.GetOne(id)
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Output
	return ctx.JSONResponse(station, 200)
}

func (ctrl *TrainStationController) createOne(ctx *atreugo.RequestCtx) error {
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
	newStation, err := ctrl.trainStationSvc.CreateOne(*station)
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Output
	return ctx.JSONResponse(newStation, 201)
}

func (ctrl *TrainStationController) updateOne(ctx *atreugo.RequestCtx) error {
	// Input
	dto := dtos.UpdateStation{}
	json.Unmarshal(ctx.PostBody(), &dto)
	id := ctx.UserValue("id").(string)

	options, err := dto.Entity()
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Process
	newStation, err := ctrl.trainStationSvc.UpdateOne(id, options)
	if err != nil {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    err.Error(),
		}, 401)
	}

	// Output
	return ctx.JSONResponse(newStation, 201)
}
