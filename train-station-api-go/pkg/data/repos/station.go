package repos

import (
	"github.com/Darkness4/train-station-api/pkg/data/ds"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/Darkness4/train-station-api/pkg/domain/repos"
)

type StationRepositoryImpl struct {
	repos.StationRepository
	ds ds.StationDataSource
}

func NewStationRepository(ds ds.StationDataSource) *StationRepositoryImpl {
	if ds == nil {
		panic("NewStationRepository: ds is nil")
	}
	repo := StationRepositoryImpl{
		ds: ds,
	}
	return &repo
}

func (repo *StationRepositoryImpl) GetManyAndCount(s string, limit int, page int, userId string) ([]*entities.Station, int64, error) {
	if limit <= 0 {
		limit = 10
	}
	if page <= 0 {
		page = 1
	}
	models, count, err := repo.ds.FindManyAndCountStation(s, limit, page)
	if err != nil {
		return nil, count, err
	}

	// Map
	values := make([]*entities.Station, 0, len(models))
	for _, val := range models {
		e, err := val.Entity(userId)
		if err != nil {
			return nil, count, err
		}
		values = append(values, e)
	}

	return values, count, nil
}

func (repo *StationRepositoryImpl) GetOne(id string, userId string) (*entities.Station, error) {
	model, err := repo.ds.FindOneStation(id)
	if err != nil {
		return nil, err
	}

	entity, err := model.Entity(userId)
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (repo *StationRepositoryImpl) CreateOne(station *entities.Station, userId string) (*entities.Station, error) {
	model, err := models.NewStationModelFromEntity(station)
	if err != nil {
		return nil, err
	}
	newModel, err := repo.ds.CreateStation(model)
	if err != nil {
		return nil, err
	}
	if station.IsFavorite != nil && *station.IsFavorite {
		if _, err := repo.ds.CreateIsFavorite(&models.IsFavoriteModel{
			UserID:    userId,
			StationID: station.RecordID,
		}); err != nil {
			return nil, err
		}
	} else if station.IsFavorite != nil && !*station.IsFavorite {
		if err := repo.ds.RemoveIsFavorite(&models.IsFavoriteModel{
			UserID:    userId,
			StationID: station.RecordID,
		}); err != nil {
			return nil, err
		}
	}

	entity, err := repo.GetOne(newModel.RecordID, userId)
	if err != nil {
		return nil, err
	}
	return entity, nil
}

func (repo *StationRepositoryImpl) CreateMany(stations []*entities.Station, userId string) ([]*entities.Station, error) {
	// Map
	values := make([]*models.StationModel, 0, len(stations))
	for _, val := range stations {
		model, err := models.NewStationModelFromEntity(val)
		if err != nil {
			return nil, err
		}

		values = append(values, model)
	}

	result, err := repo.ds.CreateManyStation(values)
	if err != nil {
		return nil, err
	}

	for _, val := range stations {
		if val.IsFavorite != nil && *val.IsFavorite {
			if _, err := repo.ds.CreateIsFavorite(&models.IsFavoriteModel{
				UserID:    userId,
				StationID: val.RecordID,
			}); err != nil {
				return nil, err
			}
		} else if val.IsFavorite != nil && !*val.IsFavorite {
			if err := repo.ds.RemoveIsFavorite(&models.IsFavoriteModel{
				UserID:    userId,
				StationID: val.RecordID,
			}); err != nil {
				return nil, err
			}
		}
	}

	// Map
	newValues := make([]*entities.Station, 0, len(result))
	for _, val := range result {
		e, err := val.Entity(userId)
		if err != nil {
			return nil, err
		}
		newValues = append(newValues, e)
	}

	return newValues, nil
}

func (repo *StationRepositoryImpl) MakeFavoriteOne(id string, isFavorite bool, userId string) (*entities.Station, error) {
	if isFavorite {
		if _, err := repo.ds.CreateIsFavorite(&models.IsFavoriteModel{
			UserID:    userId,
			StationID: id,
		}); err != nil {
			return nil, err
		}
	} else {
		if err := repo.ds.RemoveIsFavorite(&models.IsFavoriteModel{
			UserID:    userId,
			StationID: id,
		}); err != nil {
			return nil, err
		}
	}
	entity, err := repo.GetOne(id, userId)
	if err != nil {
		return nil, err
	}

	return entity, nil
}

func (repo *StationRepositoryImpl) Count(s string) (int64, error) {
	return repo.ds.CountStation(s)
}
