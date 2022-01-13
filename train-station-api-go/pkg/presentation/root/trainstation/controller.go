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
	"github.com/gofiber/fiber/v2"
)

type Controller struct {
	api        fiber.Router
	repo       station.Repository
	authFilter filters.AuthenticationFilter
	validate   *validator.Validate
}

func NewController(
	api fiber.Router,
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
	stations := ctrl.api.Group("/stations")
	stations.Get("/", func(ctx *fiber.Ctx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.getMany,
			),
		)
	})
	stations.Get("/:id", func(ctx *fiber.Ctx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.getOne,
			),
		)
	})
	stations.Patch("/:id", func(ctx *fiber.Ctx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.updateOne(ctx),
		)
	})
	stations.Post("/", func(ctx *fiber.Ctx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.createOne,
			),
		)
	})
	stations.Post("/:id/makeFavorite", func(ctx *fiber.Ctx) error {
		return filters.ExceptionFilter(ctx,
			ctrl.authFilter.Apply(ctx,
				ctrl.makeFavoriteOne,
			),
		)
	})
}

func (ctrl *Controller) getMany(ctx *fiber.Ctx, uid string) error {
	// Input
	args := &queryargs.GetMany{}
	if err := ctx.QueryParser(args); err != nil {
		return err
	}
	s := queryargs.SearchLibelle{
		Libelle: queryargs.ContainType{
			Contain: "",
		},
	}
	json.Unmarshal([]byte(args.Search), &s) // Ignore error

	limit := 10
	if args.Limit != "" && args.Limit != "0" {
		if newLimit, err := strconv.Atoi(args.Limit); err == nil && newLimit >= 0 {
			limit = newLimit
		} // Ignore error
	}
	page := 1
	if args.Page != "" {
		if newPage, err := strconv.Atoi(args.Page); err == nil && newPage >= 0 {
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
	return ctx.JSON(response)
}

func (ctrl *Controller) getOne(ctx *fiber.Ctx, uid string) error {
	// Input
	id := ctx.Params("id")

	// Process
	station, err := ctrl.repo.GetOne(id, uid)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSON(station)
}

func (ctrl *Controller) createOne(ctx *fiber.Ctx, uid string) error {
	// Input
	dto := entities.Station{}
	if err := ctx.BodyParser(&dto); err != nil {
		return err
	}

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
	return ctx.Status(fiber.StatusCreated).JSON(newStation)
}

func (ctrl *Controller) updateOne(ctx *fiber.Ctx) error {
	return ctx.Status(fiber.StatusBadRequest).JSON(&dtos.Error{
		StatusCode: fiber.StatusBadRequest,
		Message:    "The operation PATCH is deprecated.",
	})
}

func (ctrl *Controller) makeFavoriteOne(ctx *fiber.Ctx, uid string) (err error) {
	// Input
	dto := dtos.MakeFavorite{}
	if err := ctx.BodyParser(&dto); err != nil {
		return err
	}
	id := ctx.Params("id")

	// Validate DTO
	if err := ctrl.validate.Struct(&dto); err != nil {
		return err
	}

	newStation, err := ctrl.repo.MakeFavoriteOne(id, *dto.IsFavorite, uid)
	if err != nil {
		return err
	}

	// Output
	return ctx.JSON(newStation)
}
