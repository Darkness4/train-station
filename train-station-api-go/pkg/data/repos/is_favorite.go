package repos

import (
	"github.com/Darkness4/train-station-api/pkg/data/ds"
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/Darkness4/train-station-api/pkg/domain/repos"
)

type IsFavoriteRepositoryImpl struct {
	repos.IsFavoriteRepository
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
	model := &models.IsFavoriteModel{
		UserID:    userId,
		StationID: stationRecordId,
	}
	_, err := repo.ds.CreateIsFavorite(model)
	if err != nil {
		return err
	}
	return nil
}
