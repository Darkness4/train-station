package filters

import (
	"log"
	"strings"

	"firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/pkg/presentation/dtos"
	"github.com/go-playground/validator/v10"
	"github.com/savsgio/atreugo/v11"
	"github.com/spf13/viper"
	"gorm.io/gorm"
)

func ExceptionFilter(ctx *atreugo.RequestCtx, err error) error {
	if err == nil {
		return nil
	}

	// Handled errors
	// By errors
	switch err {
	case gorm.ErrRecordNotFound:
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 404,
			Message:    err.Error(),
		}, 404)
	}
	// By type
	switch err.(type) {
	case validator.ValidationErrors:
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 400,
			Message:    err.Error(),
		}, 400)
	}
	// By errorutils
	if auth.IsIDTokenInvalid(err) {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 401,
			Message:    "ID Token is invalid",
		}, 401)
	}
	// By string
	if strings.Contains(err.Error(), "FOREIGN KEY constraint failed") {
		return ctx.JSONResponse(dtos.Error{
			StatusCode: 400,
			Message:    "One of the references does not exist",
		}, 400)
	}

	// Unhandled errors
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
