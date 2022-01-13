package cmd

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core/root"
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
			if err := sl.Presentation.Server.ListenAndServe(); err != nil {
				internal.Logger.Panic(err)
			}
		},
	}
)

func init() {
	rootCmd.Flags().String("host", "0.0.0.0", "listening host (default is 0.0.0.0)")
	rootCmd.Flags().Int("port", 3000, "listening port (default is 3000)")
	rootCmd.Flags().BoolP("debug", "d", false, "enable debug")
	rootCmd.Flags().String("google-application-credentials", "./service-account.json", "location of google credentials (default is ./service-account.json)")
	viper.BindPFlag("HOST", rootCmd.Flags().Lookup("host"))
	viper.BindPFlag("PORT", rootCmd.Flags().Lookup("port"))
	viper.BindPFlag("debug", rootCmd.Flags().Lookup("debug"))
	viper.BindPFlag("GOOGLE_APPLICATION_CREDENTIALS", rootCmd.Flags().Lookup("google-application-credentials"))
}

func Execute() error {
	return rootCmd.Execute()
}
