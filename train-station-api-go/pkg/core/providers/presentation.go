package providers

import (
	"fmt"

	"github.com/Darkness4/train-station-api/pkg/domain/auth"
	"github.com/Darkness4/train-station-api/pkg/domain/station"
	"github.com/Darkness4/train-station-api/pkg/presentation/root"
	"github.com/Darkness4/train-station-api/pkg/presentation/root/trainstation"
	"github.com/atreugo/cors"
	"github.com/savsgio/atreugo/v11"
	"github.com/spf13/viper"
)

func HTTPServer(stations station.Repository, auth auth.Service) *atreugo.Atreugo {
	addr := fmt.Sprintf("%s:%d", viper.GetString("HOST"), viper.GetInt("PORT"))
	config := atreugo.Config{
		Addr:  addr,
		Debug: viper.GetBool("debug"),
	}

	// Spawn the server
	server := atreugo.New(config)
	corsConfig := cors.Config{
		AllowedOrigins: []string{"*"},
		AllowedMethods: []string{"GET", "PATCH", "OPTIONS", "POST"},
		AllowedHeaders: []string{"Access-Control-Allow-Origin", "Content-Type", "Accept", "Accept-Language", "Origin", "User-Agent", "Authorization"},
		AllowMaxAge:    3600,
	}
	server.UseAfter(cors.New(corsConfig))
	api := server.NewGroupPath("/api")
	root.NewController(server)
	trainstation.NewController(api, stations, auth)

	return server
}
