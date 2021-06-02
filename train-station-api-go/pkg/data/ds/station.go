package ds

import (
	"github.com/Darkness4/train-station-api/pkg/data/models"
	"gorm.io/gorm"
)

type StationDataSource interface {
	Create(m *models.StationModel) (*models.StationModel, error)
	CreateMany(m []*models.StationModel) ([]*models.StationModel, error)
	Update(id string, station *models.StationModel) (*models.StationModel, error)
	FindOne(id string) (*models.StationModel, error)
	FindManyAndCount(s string, limit int, page int) ([]*models.StationModel, int64, error)
	Count() (int64, error)
}

type StationDataSourceImpl struct {
	db *gorm.DB
}

func NewStationDataSource(db *gorm.DB) *StationDataSourceImpl {
	if db == nil {
		panic("StationRepository: db is nil")
	}
	return &StationDataSourceImpl{db}
}

func (ds *StationDataSourceImpl) Create(m *models.StationModel) (*models.StationModel, error) {
	result := ds.db.Create(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *StationDataSourceImpl) CreateMany(m []*models.StationModel) ([]*models.StationModel, error) {
	result := ds.db.CreateInBatches(&m, 100)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *StationDataSourceImpl) Update(id string, m *models.StationModel) (*models.StationModel, error) {
	new := models.StationModel{
		RecordID: id,
	}
	ds.db.First(&new)

	result := ds.db.Model(&new).Updates(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return &new, nil
}

func (ds *StationDataSourceImpl) FindOne(id string) (*models.StationModel, error) {
	m := &models.StationModel{
		RecordID: id,
	}
	result := ds.db.First(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *StationDataSourceImpl) FindManyAndCount(s string, limit int, page int) ([]*models.StationModel, int64, error) {
	offset := (page - 1) * limit

	var m []*models.StationModel

	result := ds.db.Select(
		"record_id",
		"dataset_id",
		"is_favorite",
		"libelle",
		"record_timestamp",
	).Where("libelle LIKE '%'||?||'%'", s).Limit(limit).Offset(offset).Order("libelle").Find(&m)

	if result.Error != nil {
		return nil, result.RowsAffected, result.Error
	}
	return m, result.RowsAffected, nil
}

func (ds *StationDataSourceImpl) Count() (int64, error) {
	var count int64

	result := ds.db.Model(&models.StationModel{}).Count(&count)
	if result.Error != nil {
		return 0, result.Error
	}

	return count, nil
}
