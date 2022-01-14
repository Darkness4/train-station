package main

import (
	"github.com/Darkness4/train-station-api/cmd"
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core"
	"github.com/sirupsen/logrus"
)

func main() {
	core.InitConfig()
	if err := cmd.Execute(); err != nil {
		internal.Logger.WithFields(logrus.Fields{
			"context": "main",
		}).Fatalf("Couldn't execute command %+v\n", err)
	}
}
