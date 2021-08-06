package ctrls

import (
	"encoding/json"
	"strconv"

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
	api        *atreugo.Router
	repo       repos.StationRepository
	authFilter filters.AuthenticationFilter
	validate   *validator.Validate
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
	ctrl := TrainStationController{
		api,
		repo,
		*filters.NewAuthenticationFilter(auth),
		validator.New(),
	}
	ctrl.buildRoutes()
	return &ctrl
}

func (ctrl *TrainStationController) buildRoutes() {
	stations := ctrl.api.NewGroupPath("/stations")
	stations.GET("/", func(ctx *atreugo.RequestCtx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.getMany,
			),
		)
	})
	stations.GET("/{id}", func(ctx *atreugo.RequestCtx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.getOne,
			),
		)
	})
	stations.PATCH("/{id}", func(ctx *atreugo.RequestCtx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.updateOne(ctx),
		)
	})
	stations.POST("/", func(ctx *atreugo.RequestCtx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.createOne,
			),
		)
	})
	stations.POST("/{id}/makeFavorite", func(ctx *atreugo.RequestCtx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.makeFavoriteOne,
			),
		)
	})
}

func (ctrl *TrainStationController) getMany(ctx *atreugo.RequestCtx, uid string) error {
	// Input
	args := ctx.QueryArgs()
	sRaw := args.Peek("s")
	s := queryargs.SearchLibelle{
		Libelle: queryargs.ContainType{
			Contain: "",
		},
	}
	json.Unmarshal(sRaw, &s) // Ignore error

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

func (ctrl *TrainStationController) getOne(ctx *atreugo.RequestCtx, uid string) error {
	// Input
	id := ctx.UserValue("id").(string)

	// Process
	station, err := ctrl.repo.GetOne(id, uid)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSONResponse(station, 200)
}

func (ctrl *TrainStationController) createOne(ctx *atreugo.RequestCtx, uid string) error {
	// Input
	dto := entities.Station{}
	json.Unmarshal(ctx.PostBody(), &dto)

	// Validate DTO
	if err := ctrl.validate.Struct(&dto); err != nil {
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
	return ctx.JSONResponse(&dtos.Error{
		StatusCode: 400,
		Message:    "The operation PATCH is deprecated.",
	}, 400)
}

func (ctrl *TrainStationController) makeFavoriteOne(ctx *atreugo.RequestCtx, uid string) (err error) {
	// Input
	dto := dtos.MakeFavorite{}
	json.Unmarshal(ctx.PostBody(), &dto)
	id := ctx.UserValue("id").(string)

	// Validate DTO
	if err := ctrl.validate.Struct(&dto); err != nil {
		return err
	}

	newStation, err := ctrl.repo.MakeFavoriteOne(id, *dto.IsFavorite, uid)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSONResponse(newStation, 200)
}
