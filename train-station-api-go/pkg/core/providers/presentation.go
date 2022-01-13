package providers

import (
	"fmt"

	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/domain/auth"
	"github.com/Darkness4/train-station-api/pkg/domain/station"
	"github.com/Darkness4/train-station-api/pkg/presentation/root"
	"github.com/Darkness4/train-station-api/pkg/presentation/root/trainstation"
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/sirupsen/logrus"
)

func HTTPServer(stations station.Repository, auth auth.Service) *fiber.App {
	internal.Logger.Debug("Provide HTTPServer")
	server := fiber.New(fiber.Config{
		Prefork:           true,
		CaseSensitive:     false,
		StrictRouting:     false,
		AppName:           "A SNCF API",
		EnablePrintRoutes: true,
	})

	server.Use(cors.New(cors.Config{
		AllowOrigins: "*",
		AllowMethods: "GET, PATCH, OPTIONS, POST",
		AllowHeaders: "Access-Control-Allow-Origin, Content-Type, Accept, Accept-Language, Origin, User-Agent, Authorization",
		MaxAge:       3600,
	}))
	server.Use(func(c *fiber.Ctx) error {
		// Log each request
		internal.Logger.WithFields(logrus.Fields{
			"context": "HTTPServer",
			"addr":    fmt.Sprintf("%p", server),
			"method":  c.Method(),
			"path":    c.Path(),
		})

		// Go to next middleware
		return c.Next()
	})
	api := server.Group("/api")
	root.NewController(server)
	trainstation.NewController(api, stations, auth)

	return server
}
