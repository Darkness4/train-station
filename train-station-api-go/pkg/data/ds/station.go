package ds

import (
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"gorm.io/gorm"
)

type StationDataSource interface {
	CreateStation(m *models.StationModel) (*models.StationModel, error)
	CreateManyStation(m []*models.StationModel) ([]*models.StationModel, error)
	UpdateStation(id string, station *models.StationModel) (*models.StationModel, error)
	FindOneStation(id string) (*models.StationModel, error)
	FindManyAndCountStation(s string, limit int, page int) ([]*models.StationModel, int64, error)
	CountStation(s string) (int64, error)
	CreateIsFavorite(m *models.IsFavoriteModel) (*models.IsFavoriteModel, error)
	RemoveIsFavorite(m *models.IsFavoriteModel) error
}

type StationDataSourceImpl struct {
	StationDataSource
	db *gorm.DB
}

func NewStationDataSource(db *gorm.DB) *StationDataSourceImpl {
	if db == nil {
		panic("StationRepository: db is nil")
	}
	return &StationDataSourceImpl{
		db: db,
	}
}

func (ds *StationDataSourceImpl) CreateStation(m *models.StationModel) (*models.StationModel, error) {
	result := ds.db.Create(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *StationDataSourceImpl) CreateManyStation(m []*models.StationModel) ([]*models.StationModel, error) {
	result := ds.db.CreateInBatches(&m, 100)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *StationDataSourceImpl) UpdateStation(id string, m *models.StationModel) (*models.StationModel, error) {
	new := models.StationModel{
		RecordID: id,
	}
	result := ds.db.First(&new)
	if result.Error != nil {
		return nil, result.Error
	}

	result = ds.db.Model(&new).Updates(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return &new, nil
}

func (ds *StationDataSourceImpl) FindOneStation(id string) (*models.StationModel, error) {
	m := &models.StationModel{
		RecordID: id,
	}
	result := ds.db.Preload("IsFavorites").First(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *StationDataSourceImpl) FindManyAndCountStation(s string, limit int, page int) ([]*models.StationModel, int64, error) {
	offset := (page - 1) * limit

	var m []*models.StationModel

	result := ds.db.Preload("IsFavorites").Select(
		"record_id",
		"dataset_id",
		"libelle",
		"record_timestamp",
	).Where("libelle LIKE '%'||?||'%'", s).Limit(limit).Offset(offset).Order("libelle").Find(&m)

	if result.Error != nil {
		return nil, result.RowsAffected, result.Error
	}
	return m, result.RowsAffected, nil
}

func (ds *StationDataSourceImpl) CountStation(s string) (int64, error) {
	var count int64

	result := ds.db.Model(&models.StationModel{}).Where("libelle LIKE '%'||?||'%'", s).Count(&count)
	if result.Error != nil {
		return 0, result.Error
	}

	return count, nil
}

func (ds *StationDataSourceImpl) CreateIsFavorite(m *models.IsFavoriteModel) (*models.IsFavoriteModel, error) {
	result := ds.db.Create(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *StationDataSourceImpl) RemoveIsFavorite(m *models.IsFavoriteModel) error {
	result := ds.db.Unscoped().Delete(m)

	if result.Error != nil {
		return result.Error
	}
	return nil
}
