package trainstation

import (
	"encoding/json"
	"strconv"

	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/domain/auth"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/Darkness4/train-station-api/pkg/domain/station"
	"github.com/Darkness4/train-station-api/pkg/presentation/dtos"
	"github.com/Darkness4/train-station-api/pkg/presentation/filters"
	"github.com/Darkness4/train-station-api/pkg/presentation/queryargs"
	"github.com/go-playground/validator/v10"
	"github.com/savsgio/atreugo/v11"
)

type Controller struct {
	api        *atreugo.Router
	repo       station.Repository
	authFilter filters.AuthenticationFilter
	validate   *validator.Validate
}

func NewController(
	api *atreugo.Router,
	repo station.Repository,
	auth auth.Service,
) *Controller {
	if api == nil {
		internal.Logger.Panic("NewController: api is nil")
	}
	if repo == nil {
		internal.Logger.Panic("NewController: repo is nil")
	}
	if auth == nil {
		internal.Logger.Panic("NewController: auth is nil")
	}
	ctrl := Controller{
		api,
		repo,
		*filters.NewAuthenticationFilter(auth),
		validator.New(),
	}
	ctrl.buildRoutes()
	return &ctrl
}

func (ctrl *Controller) buildRoutes() {
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

func (ctrl *Controller) getMany(ctx *atreugo.RequestCtx, uid string) error {
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

func (ctrl *Controller) getOne(ctx *atreugo.RequestCtx, uid string) error {
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

func (ctrl *Controller) createOne(ctx *atreugo.RequestCtx, uid string) error {
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

func (ctrl *Controller) updateOne(ctx *atreugo.RequestCtx) error {
	return ctx.JSONResponse(&dtos.Error{
		StatusCode: 400,
		Message:    "The operation PATCH is deprecated.",
	}, 400)
}

func (ctrl *Controller) makeFavoriteOne(ctx *atreugo.RequestCtx, uid string) (err error) {
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
