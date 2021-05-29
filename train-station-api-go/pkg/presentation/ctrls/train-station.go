package ctrls

import (
	"encoding/json"
	"strconv"

	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/Darkness4/train-station-api/pkg/domain/svcs"
	"github.com/Darkness4/train-station-api/pkg/presentation/ctrls/dtos"
	"github.com/Darkness4/train-station-api/pkg/presentation/filters"
	"github.com/go-playground/validator/v10"
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
		return filters.ExceptionFilter(ctx, ctrl.getMany(ctx))
	})
	stations.GET("/{id}", func(ctx *atreugo.RequestCtx) error {
		return filters.ExceptionFilter(ctx, ctrl.getOne(ctx))
	})
	stations.PATCH("/{id}", func(ctx *atreugo.RequestCtx) error {

		return filters.ExceptionFilter(ctx, ctrl.updateOne(ctx))
	})
	stations.POST("/", func(ctx *atreugo.RequestCtx) error {
		return filters.ExceptionFilter(ctx, ctrl.createOne(ctx))
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
			return err
		}
		limit = newLimit
	}
	pageStr := string(args.Peek("page"))
	page := 1
	if pageStr != "" {
		newPage, err := strconv.Atoi(pageStr)
		if err != nil {
			return err
		}
		page = newPage
	}

	// Process
	stations, count, err := ctrl.trainStationSvc.GetManyAndCount(s, limit, page)
	if err != nil {
		return err
	}
	total, err := ctrl.trainStationSvc.Total()
	if err != nil {
		return err
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
		return err
	}

	// Output
	return ctx.JSONResponse(station, 200)
}

func (ctrl *TrainStationController) createOne(ctx *atreugo.RequestCtx) error {
	// Input
	dto := entities.Station{}
	json.Unmarshal(ctx.PostBody(), &dto)

	// Validate
	validate := validator.New()
	if err := validate.Struct(&dto); err != nil {
		return err
	}

	// Process
	newStation, err := ctrl.trainStationSvc.CreateOne(dto)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSONResponse(newStation, 201)
}

func (ctrl *TrainStationController) updateOne(ctx *atreugo.RequestCtx) error {
	// Input
	dto := entities.Station{}
	json.Unmarshal(ctx.PostBody(), &dto)
	id := ctx.UserValue("id").(string)

	// Process
	newStation, err := ctrl.trainStationSvc.UpdateOne(id, dto)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSONResponse(newStation, 201)
}
