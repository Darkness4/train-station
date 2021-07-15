package repos

import (
	"github.com/Darkness4/train-station-api/pkg/data/ds"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type StationRepositoryImpl struct {
	ds ds.StationDataSource
}

func NewStationRepository(ds ds.StationDataSource) *StationRepositoryImpl {
	if ds == nil {
		panic("TrainStationService: repo is nil")
	}
	repo := StationRepositoryImpl{
		ds: ds,
	}
	return &repo
}

func (repo *StationRepositoryImpl) GetManyAndCount(s string, limit int, page int) ([]*entities.Station, int64, error) {
	if limit <= 0 {
		limit = 10
	}
	if page <= 0 {
		page = 1
	}
	models, count, err := repo.ds.FindManyAndCount(s, limit, page)
	if err != nil {
		return nil, count, err
	}

	// Map
	values := make([]*entities.Station, 0, len(models))
	for _, val := range models {
		e, err := val.Entity()
		if err != nil {
			return nil, count, err
		}
		values = append(values, e)
	}

	return values, count, nil
}

func (repo *StationRepositoryImpl) GetOne(id string) (*entities.Station, error) {
	model, err := repo.ds.FindOne(id)
	if err != nil {
		return nil, err
	}

	entity, err := model.Entity()
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (repo *StationRepositoryImpl) CreateOne(station *entities.Station) (*entities.Station, error) {
	model, err := models.NewStationModelFromEntity(station)
	if err != nil {
		return nil, err
	}
	newModel, err := repo.ds.Create(model)
	if err != nil {
		return nil, err
	}

	entity, err := newModel.Entity()
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (repo *StationRepositoryImpl) CreateMany(stations []*entities.Station) ([]*entities.Station, error) {
	// Map
	values := make([]*models.StationModel, 0, len(stations))
	for _, val := range stations {
		model, err := models.NewStationModelFromEntity(val)
		if err != nil {
			return nil, err
		}

		values = append(values, model)
	}

	result, err := repo.ds.CreateMany(values)
	if err != nil {
		return nil, err
	}

	// Map
	newValues := make([]*entities.Station, 0, len(result))
	for _, val := range result {
		e, err := val.Entity()
		if err != nil {
			return nil, err
		}
		newValues = append(newValues, e)
	}

	return newValues, nil
}

func (repo *StationRepositoryImpl) UpdateOne(id string, station *entities.Station) (*entities.Station, error) {
	model, err := models.NewStationModelFromEntity(station)
	if err != nil {
		return nil, err
	}
	newModel, err := repo.ds.Update(id, model)
	if err != nil {
		return nil, err
	}
	entity, err := newModel.Entity()
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (repo *StationRepositoryImpl) Count(s string) (int64, error) {
	return repo.ds.Count(s)
}
