package station

import (
	"encoding/json"
	"fmt"
	"time"

	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/data/database/isfavorite"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"github.com/valyala/fasthttp"
)

type StationRepositoryImpl struct {
	ds   DataSource
	http *fasthttp.Client
}

func NewRepository(ds DataSource, http *fasthttp.Client) *StationRepositoryImpl {
	if ds == nil {
		internal.Logger.Panic("NewStationRepository: ds is nil")
	} else if http == nil {
		internal.Logger.Panic("NewStationRepository: http is nil")
	}
	return &StationRepositoryImpl{
		ds:   ds,
		http: http,
	}
}

func (repo *StationRepositoryImpl) Preload() error {
	req := fasthttp.AcquireRequest()
	resp := fasthttp.AcquireResponse()
	defer func() {
		fasthttp.ReleaseResponse(resp)
		fasthttp.ReleaseRequest(req)
	}()

	req.SetRequestURI("https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json")
	req.Header.SetMethod(fasthttp.MethodGet)
	if err := repo.http.DoTimeout(req, resp, 2*time.Minute); err != nil {
		return err
	}
	if resp.StatusCode() != fasthttp.StatusOK {
		return fmt.Errorf("status code wasn't 200 but was %d", resp.StatusCode())
	}
	var data []*entities.Station
	if err := json.Unmarshal(resp.Body(), &data); err != nil {
		return err
	}

	result, err := repo.CreateMany(data, "0")
	if err != nil {
		return err
	}

	internal.Logger.Printf("InitializeDatabase: Changed lines %d\n", len(result))
	return nil
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
	model, err := NewModelFromEntity(station)
	if err != nil {
		return nil, err
	}
	newModel, err := repo.ds.CreateStation(model)
	if err != nil {
		return nil, err
	}
	if station.IsFavorite != nil && *station.IsFavorite {
		if _, err := repo.ds.CreateIsFavorite(&isfavorite.Model{
			UserID:    userId,
			StationID: station.RecordID,
		}); err != nil {
			return nil, err
		}
	} else if station.IsFavorite != nil && !*station.IsFavorite {
		if err := repo.ds.RemoveIsFavorite(&isfavorite.Model{
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
	values := make([]*Model, 0, len(stations))
	for _, val := range stations {
		model, err := NewModelFromEntity(val)
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
			if _, err := repo.ds.CreateIsFavorite(&isfavorite.Model{
				UserID:    userId,
				StationID: val.RecordID,
			}); err != nil {
				return nil, err
			}
		} else if val.IsFavorite != nil && !*val.IsFavorite {
			if err := repo.ds.RemoveIsFavorite(&isfavorite.Model{
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
		if _, err := repo.ds.CreateIsFavorite(&isfavorite.Model{
			UserID:    userId,
			StationID: id,
		}); err != nil {
			return nil, err
		}
	} else {
		if err := repo.ds.RemoveIsFavorite(&isfavorite.Model{
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
