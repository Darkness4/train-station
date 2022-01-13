package cmd

import (
	"fmt"

	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core/root"
	"github.com/sirupsen/logrus"
	"github.com/spf13/cobra"
	"github.com/spf13/viper"
)

var (
	rootCmd = &cobra.Command{
		Use:   "train-station-api",
		Short: "A SNCF API.",
		Long:  `A SNCF API built made by Darkness4, based on atreugo, gorm and cobra.`,
		Run: func(cmd *cobra.Command, args []string) {
			sl := root.ComponentInit()
			defer sl.Dispose()

			// Run
			addr := fmt.Sprintf("%s:%d", viper.GetString("HOST"), viper.GetInt("PORT"))
			if err := sl.Presentation.Server.Listen(addr); err != nil {
				internal.Logger.Panic(err)
			}
		},
	}
)

func init() {
	// HTTP Server
	rootCmd.PersistentFlags().String("host", "0.0.0.0", "listening host (env: HOST)")
	if err := viper.BindPFlag("HOST", rootCmd.PersistentFlags().Lookup("host")); err != nil {
		internal.Logger.WithFields(logrus.Fields{
			"context": "root",
		}).Panic(err.Error())
	}
	rootCmd.PersistentFlags().Int("port", 3000, "listening port (env: PORT)")
	if err := viper.BindPFlag("PORT", rootCmd.PersistentFlags().Lookup("port")); err != nil {
		internal.Logger.WithFields(logrus.Fields{
			"context": "root",
		}).Panic(err.Error())
	}

	// Logging
	rootCmd.PersistentFlags().String("log-level", "info", "panic, fatal, error, warn/warning, info, debug, trace (env: LOG_LEVEL)")
	if err := viper.BindPFlag("LOG_LEVEL", rootCmd.PersistentFlags().Lookup("log-level")); err != nil {
		internal.Logger.WithFields(logrus.Fields{
			"context": "root",
		}).Panic(err.Error())
	}

	rootCmd.Flags().String("google-application-credentials", "./service-account.json", "location of google credentials (default is ./service-account.json)")
	if err := viper.BindPFlag("GOOGLE_APPLICATION_CREDENTIALS", rootCmd.Flags().Lookup("google-application-credentials")); err != nil {
		internal.Logger.WithFields(logrus.Fields{
			"context": "root",
		}).Panic(err.Error())
	}
}

func Execute() error {
	return rootCmd.Execute()
}
