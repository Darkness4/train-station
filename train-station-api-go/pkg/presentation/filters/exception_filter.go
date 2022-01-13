package filters

import (
	"log"
	"strings"

	"firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/pkg/presentation/dtos"
	"github.com/go-playground/validator/v10"
	"github.com/gofiber/fiber/v2"
	"github.com/spf13/viper"
	"gorm.io/gorm"
)

func ExceptionFilter(ctx *fiber.Ctx, err error) error {
	if err == nil {
		return nil
	}

	// Handled errors
	// By errors
	switch err {
	case gorm.ErrRecordNotFound:
		return ctx.Status(fiber.StatusNotFound).JSON(dtos.Error{
			StatusCode: fiber.StatusNotFound,
			Message:    err.Error(),
		})
	}
	// By type
	switch err.(type) {
	case validator.ValidationErrors:
		return ctx.Status(fiber.StatusBadRequest).JSON(dtos.Error{
			StatusCode: fiber.StatusBadRequest,
			Message:    err.Error(),
		})
	}
	// By errorutils
	if auth.IsIDTokenInvalid(err) {
		return ctx.Status(fiber.StatusUnauthorized).JSON(dtos.Error{
			StatusCode: fiber.StatusUnauthorized,
			Message:    "ID Token is invalid",
		})
	}
	// By string
	if strings.Contains(err.Error(), "FOREIGN KEY constraint failed") {
		return ctx.Status(fiber.StatusBadRequest).JSON(dtos.Error{
			StatusCode: fiber.StatusBadRequest,
			Message:    "One of the references does not exist",
		})
	}

	// Unhandled errors
	log.Printf("Internal Server Error: %+v\n", err)
	if viper.GetString("LOG_LEVEL") == "debug" {
		return ctx.Status(fiber.StatusInternalServerError).JSON(dtos.Error{
			StatusCode: fiber.StatusInternalServerError,
			Message:    err.Error(),
		})
	} else {
		return ctx.Status(fiber.StatusInternalServerError).JSON(dtos.Error{
			StatusCode: fiber.StatusInternalServerError,
			Message:    "Internal Server Error",
		})
	}
}
