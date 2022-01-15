package core

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/internal/projectpath"
	"github.com/sirupsen/logrus"
	"github.com/spf13/viper"
)

func InitConfig() {
	logger := internal.Logger.WithFields(logrus.Fields{
		"context": "InitConfig",
	})
	viper.AddConfigPath(projectpath.Root)
	viper.SetConfigType("env")
	viper.SetConfigName(".env")
	if err := viper.ReadInConfig(); err != nil {
		logger.Printf(".env couldn't be loaded %+v\n", err)
	}
	viper.SetConfigName(".env.local")
	if err := viper.MergeInConfig(); err != nil {
		logger.Printf(".env.local couldn't be loaded %+v\n", err)
	}

	viper.AutomaticEnv()
	logger.Printf("Config loaded: %+v\n", viper.AllKeys())
}

func InitTestConfig() {
	logger := internal.Logger.WithFields(logrus.Fields{
		"context": "InitTestConfig",
	})
	viper.AddConfigPath(projectpath.Root)
	viper.SetConfigType("env")
	viper.SetConfigName(".env.test")
	if err := viper.ReadInConfig(); err != nil {
		logger.Printf(".env.test couldn't be loaded %+v\n", err)
	}
	logger.Printf("Config loaded: %+v\n", viper.AllSettings())
}
