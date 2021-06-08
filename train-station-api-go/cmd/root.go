package cmd

import (
	"fmt"
	"log"

	"github.com/Darkness4/train-station-api/pkg/core"
	"github.com/Darkness4/train-station-api/pkg/presentation/ctrls"
	"github.com/savsgio/atreugo/v11"
	"github.com/spf13/cobra"
	"github.com/spf13/viper"
)

var (
	host string
	port int

	rootCmd = &cobra.Command{
		Use:   "train-station-api",
		Short: "A SNCF API.",
		Long:  `A SNCF API built made by Darkness4, based on atreugo, gorm and cobra.`,
		Run: func(cmd *cobra.Command, args []string) {
			// Read config
			if viper.GetBool("debug") {
				log.Println("Debug is enabled. Do not use in production!")
			}
			addr := fmt.Sprintf("%s:%d", viper.GetString("HOST"), viper.GetInt("PORT"))
			config := atreugo.Config{
				Addr:  addr,
				Debug: viper.GetBool("debug"),
			}

			// Spawn service locator
			sl, err := core.NewServiceLocator()
			if err != nil {
				panic(err)
			}

			// Spawn the server
			server := atreugo.New(config)
			server.UseBefore(func(ctx *atreugo.RequestCtx) error {
				ctx.Response.Header.Set("Access-Control-Allow-Origin", "*")
				ctx.Request.Header.Set("Access-Control-Allow-Methods", "GET, PATCH, PUT, OPTIONS, POST, DELETE, HEAD")

				return ctx.Next()
			})
			api := server.NewGroupPath("/api")
			ctrls.NewRootController(server)
			ctrls.NewTrainStationController(api, sl.StationRepository)

			// Run
			if err := server.ListenAndServe(); err != nil {
				panic(err)
			}
		},
	}
)

func initConfig() {
	viper.AutomaticEnv()
}

func init() {
	cobra.OnInitialize(initConfig)

	rootCmd.Flags().StringVar(&host, "host", "0.0.0.0", "listening host (default is 0.0.0.0)")
	rootCmd.Flags().IntVar(&port, "port", 3000, "listening port (default is 3000)")
	rootCmd.Flags().BoolP("debug", "d", false, "enable debug")
	viper.BindPFlag("HOST", rootCmd.Flags().Lookup("host"))
	viper.BindPFlag("PORT", rootCmd.Flags().Lookup("port"))
	viper.BindPFlag("debug", rootCmd.Flags().Lookup("debug"))
}

func Execute() error {
	return rootCmd.Execute()
}
