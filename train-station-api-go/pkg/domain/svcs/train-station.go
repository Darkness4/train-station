package svcs

import (
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
		e, err := val.Entity()
		if err != nil {
			return nil, count, err
		}
		values = append(values, *e)
	}

	return values, count, nil
}

func (svc *TrainStationService) GetOne(id string) (*entities.Station, error) {
	model, err := svc.repo.FindOne(id)
	if err != nil {
		return nil, err
	}

	entity, err := model.Entity()
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (svc *TrainStationService) CreateOne(station entities.Station) (*entities.Station, error) {
	model, err := models.NewStationModelFromEntity(&station)
	if err != nil {
		return nil, err
	}
	newModel, err := svc.repo.Create(model)
	if err != nil {
		return nil, err
	}

	entity, err := newModel.Entity()
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (svc *TrainStationService) CreateMany(stations []entities.Station) ([]entities.Station, error) {
	// Map
	values := make([]models.StationModel, 0, len(stations))
	for _, val := range stations {
		model, err := models.NewStationModelFromEntity(&val)
		if err != nil {
			return nil, err
		}

		values = append(values, *model)
	}

	result, err := svc.repo.CreateMany(values)
	if err != nil {
		return nil, err
	}

	// Map
	newValues := make([]entities.Station, 0, len(result))
	for _, val := range result {
		e, err := val.Entity()
		if err != nil {
			return nil, err
		}
		newValues = append(newValues, *e)
	}

	return newValues, nil
}

func (svc *TrainStationService) UpdateOne(id string, station entities.Station) (*entities.Station, error) {
	model, err := models.NewStationModelFromEntity(&station)
	if err != nil {
		return nil, err
	}
	newModel, err := svc.repo.Update(id, model)
	if err != nil {
		return nil, err
	}
	entity, err := newModel.Entity()
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (svc *TrainStationService) Total() (int64, error) {
	return svc.repo.Count()
}
