package svcs

import (
	"fmt"

	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type TrainStationService struct {
	mockDb map[string]models.StationModel
}

func NewTrainStationService() *TrainStationService {
	svc := TrainStationService{}
	svc.mockDb = make(map[string]models.StationModel)
	return &svc
}

func (svc *TrainStationService) GetManyStations() ([]entities.Station, error) {
	values := make([]entities.Station, 0, len(svc.mockDb))
	for _, val := range svc.mockDb {
		values = append(values, val.Entity())
	}

	return values, nil
}

func (svc *TrainStationService) GetOneStation(id string) (*entities.Station, error) {
	if val, ok := svc.mockDb[id]; ok {
		entity := val.Entity()
		return &entity, nil
	} else {
		return nil, fmt.Errorf("entity with id %s doesn't exists", id)
	}
}

func (svc *TrainStationService) CreateOneStation(station entities.Station) (*entities.Station, error) {
	if _, ok := svc.mockDb[station.RecordID]; !ok {
		svc.mockDb[station.RecordID] = models.NewStationModelFromEntity(station)

		entity := svc.mockDb[station.RecordID].Entity()
		return &entity, nil
	} else {
		return nil, fmt.Errorf("entity with id %s already exists", station.RecordID)
	}
}

func (svc *TrainStationService) UpdateOneStation(id string, options entities.StationOptions) (*entities.Station, error) {
	if old, ok := svc.mockDb[id]; ok {
		new := old.Merge(options)
		svc.mockDb[id] = new

		entity := svc.mockDb[id].Entity()
		return &entity, nil
	} else {
		return nil, fmt.Errorf("entity with id %s doesn't exists", id)
	}
}
