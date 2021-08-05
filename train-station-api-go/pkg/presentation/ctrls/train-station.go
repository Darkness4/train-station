package ctrls

import (
	"encoding/json"
	"strconv"
	"strings"

	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/Darkness4/train-station-api/pkg/domain/repos"
	"github.com/Darkness4/train-station-api/pkg/domain/services"
	"github.com/Darkness4/train-station-api/pkg/presentation/ctrls/dtos"
	"github.com/Darkness4/train-station-api/pkg/presentation/ctrls/queryargs"
	"github.com/Darkness4/train-station-api/pkg/presentation/filters"
	"github.com/go-playground/validator/v10"
	"github.com/savsgio/atreugo/v11"
)

type TrainStationController struct {
	api  *atreugo.Router
	repo repos.StationRepository
	auth services.AuthService
}

func NewTrainStationController(
	api *atreugo.Router,
	repo repos.StationRepository,
	auth services.AuthService,
) *TrainStationController {
	if api == nil {
		panic("NewTrainStationController: api is nil")
	}
	if repo == nil {
		panic("NewTrainStationController: repo is nil")
	}
	if auth == nil {
		panic("NewTrainStationController: auth is nil")
	}
	ctrl := TrainStationController{api, repo, auth}
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
	authorization := string(ctx.RequestCtx.Request.Header.Peek("Authorization"))
	idToken := strings.TrimSpace(strings.Replace(authorization, "Bearer", "", 1))
	uid, err := ctrl.auth.VerifyIDToken(ctx, idToken)
	if err != nil {
		return err
	}

	args := ctx.QueryArgs()
	sRaw := args.Peek("s")
	s := queryargs.SearchLibelle{
		Libelle: queryargs.ContainType{
			Contain: "",
		},
	}
	json.Unmarshal(sRaw, &s)

	limitStr := string(args.Peek("limit"))
	limit := 10
	if limitStr != "" && limitStr != "0" {
		if newLimit, err := strconv.Atoi(limitStr); err == nil && newLimit >= 0 {
			limit = newLimit
		} // Ignore error
	}
	pageStr := string(args.Peek("page"))
	page := 1
	if pageStr != "" {
		if newPage, err := strconv.Atoi(pageStr); err == nil && newPage >= 0 {
			page = newPage
		} // Ignore error
	}

	// Process
	stations, count, err := ctrl.repo.GetManyAndCount(s.Libelle.Contain, limit, page, uid)
	if err != nil {
		return err
	}
	total, err := ctrl.repo.Count(s.Libelle.Contain)
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
	authorization := string(ctx.RequestCtx.Request.Header.Peek("Authorization"))
	idToken := strings.TrimSpace(strings.Replace(authorization, "Bearer", "", 1))
	uid, err := ctrl.auth.VerifyIDToken(ctx, idToken)
	if err != nil {
		return err
	}

	id := ctx.UserValue("id").(string)

	// Process
	station, err := ctrl.repo.GetOne(id, uid)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSONResponse(station, 200)
}

func (ctrl *TrainStationController) createOne(ctx *atreugo.RequestCtx) error {
	// Input
	authorization := string(ctx.RequestCtx.Request.Header.Peek("Authorization"))
	idToken := strings.TrimSpace(strings.Replace(authorization, "Bearer", "", 1))
	uid, err := ctrl.auth.VerifyIDToken(ctx, idToken)
	if err != nil {
		return err
	}

	dto := entities.Station{}
	json.Unmarshal(ctx.PostBody(), &dto)

	// Validate
	validate := validator.New()
	if err := validate.Struct(&dto); err != nil {
		return err
	}

	// Process
	newStation, err := ctrl.repo.CreateOne(&dto, uid)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSONResponse(newStation, 201)
}

func (ctrl *TrainStationController) updateOne(ctx *atreugo.RequestCtx) error {
	// Input
	authorization := string(ctx.RequestCtx.Request.Header.Peek("Authorization"))
	idToken := strings.TrimSpace(strings.Replace(authorization, "Bearer", "", 1))
	uid, err := ctrl.auth.VerifyIDToken(ctx, idToken)
	if err != nil {
		return err
	}

	dto := entities.Station{}
	json.Unmarshal(ctx.PostBody(), &dto)
	id := ctx.UserValue("id").(string)

	// Process
	newStation, err := ctrl.repo.UpdateOne(id, &dto, uid)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSONResponse(newStation, 200)
}
