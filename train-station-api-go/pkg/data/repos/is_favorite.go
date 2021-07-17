package repos

import (
	"github.com/Darkness4/train-station-api/pkg/data/ds"
	"github.com/Darkness4/train-station-api/pkg/data/models"
)

type IsFavoriteRepositoryImpl struct {
	ds ds.StationDataSource
}

func NewIsFavoriteRepository(ds ds.StationDataSource) *IsFavoriteRepositoryImpl {
	if ds == nil {
		panic("NewIsFavoriteRepository: ds is nil")
	}
	repo := IsFavoriteRepositoryImpl{
		ds: ds,
	}
	return &repo
}

func (repo *IsFavoriteRepositoryImpl) CreateOne(stationRecordId string, userId string) error {
	station, err := repo.ds.FindOneStation(stationRecordId)
	if err != nil {
		return err
	}
	model := &models.IsFavoriteModel{
		UserID:    userId,
		StationID: station.RecordID,
		Station:   station,
	}
	_, err = repo.ds.CreateIsFavorite(model)
	if err != nil {
		return err
	}
	return nil
}
