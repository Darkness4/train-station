package core

import "github.com/Darkness4/train-station-api/pkg/domain/svcs"

type ServiceLocator struct {
	TrainStationService *svcs.TrainStationService
}

func NewServiceLocator() *ServiceLocator {
	return &ServiceLocator{
		svcs.NewTrainStationService(),
	}
}
