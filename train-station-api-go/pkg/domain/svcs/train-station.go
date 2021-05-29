package svcs

import (
	"fmt"

	"github.com/Darkness4/train-station-api/pkg/data/db"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type TrainStationService struct {
	repo *db.StationRepository
}

func NewTrainStationService(repo *db.StationRepository) *TrainStationService {
	if repo == nil {
		panic("TrainStationService: repo is nil")
	}
	svc := TrainStationService{
		repo: repo,
	}
	return &svc
}

func (svc *TrainStationService) GetManyAndCount(s string, limit int, page int) ([]entities.Station, int64, error) {
	if limit <= 0 {
		limit = 10
	}
	if page <= 0 {
		page = 1
	}
	models, count, err := svc.repo.FindManyAndCount(s, limit, page)
	if err != nil {
		// TODO: Better error handling
		return nil, count, err
	}

	// Map
	values := make([]entities.Station, 0, len(models))
	for _, val := range models {
		values = append(values, val.Entity())
	}

	return values, count, nil
}

func (svc *TrainStationService) GetOne(id string) (*entities.Station, error) {
	model, err := svc.repo.FindOne(id)
	if err != nil {
		return nil, err
	}

	entity := model.Entity()
	return &entity, nil
}

func (svc *TrainStationService) CreateOne(station entities.Station) (*entities.Station, error) {
	model := models.NewStationModelFromEntity(station)
	newModel, err := svc.repo.Create(&model)
	if err != nil {
		return nil, err
	}

	entity := newModel.Entity()
	return &entity, nil
}

func (svc *TrainStationService) UpdateOne(id string, station entities.Station) (*entities.Station, error) {
	fmt.Printf("%+v\n", station)
	model := models.NewStationModelFromEntity(station)
	newModel, err := svc.repo.Update(id, &model)
	if err != nil {
		return nil, err
	}

	entity := newModel.Entity()
	return &entity, nil
}

func (svc *TrainStationService) Total() (int64, error) {
	return svc.repo.Count()
}
