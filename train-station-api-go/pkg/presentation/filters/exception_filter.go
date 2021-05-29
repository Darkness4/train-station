package filters

import (
	"errors"
	"log"

	"github.com/Darkness4/train-station-api/pkg/presentation/ctrls/dtos"
	"github.com/go-playground/validator/v10"
	"github.com/savsgio/atreugo/v11"
	"github.com/spf13/viper"
	"gorm.io/gorm"
)

func ExceptionFilter(ctx *atreugo.RequestCtx, err error) error {
	if errors.Is(err, gorm.ErrRecordNotFound) {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 404,
			Message:    err.Error(),
		}, 404)
	} else if _, ok := err.(validator.ValidationErrors); ok {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 400,
			Message:    err.Error(),
		}, 400)
	} else if err != nil {
		log.Printf("Internal Server Error: %+v\n", err)
		if viper.GetBool("debug") {
			return ctx.JSONResponse(dtos.Error{
				StatusCode: 500,
				Message:    err.Error(),
			}, 500)
		} else {
			return ctx.JSONResponse(dtos.Error{
				StatusCode: 500,
				Message:    "Internal Server Error",
			}, 500)
		}
	}

	// Unhandled error. Will panic the server.
	return nil
}
