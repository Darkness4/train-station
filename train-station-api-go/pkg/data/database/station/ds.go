package station

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/data/database/isfavorite"
	"gorm.io/gorm"
)

type DataSource interface {
	CreateStation(m *Model) (*Model, error)
	CreateManyStation(m []*Model) ([]*Model, error)
	UpdateStation(id string, station *Model) (*Model, error)
	FindOneStation(id string) (*Model, error)
	FindManyAndCountStation(s string, limit int, page int) ([]*Model, int64, error)
	CountStation(s string) (int64, error)
	CreateIsFavorite(m *isfavorite.Model) (*isfavorite.Model, error)
	RemoveIsFavorite(m *isfavorite.Model) error
}

type DataSourceImpl struct {
	DataSource
	db *gorm.DB
}

func NewDataSource(db *gorm.DB) *DataSourceImpl {
	if db == nil {
		internal.Logger.Panic("NewDataSource: db is nil")
	}
	return &DataSourceImpl{
		db: db,
	}
}

func (ds *DataSourceImpl) CreateStation(m *Model) (*Model, error) {
	result := ds.db.Create(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *DataSourceImpl) CreateManyStation(m []*Model) ([]*Model, error) {
	result := ds.db.CreateInBatches(&m, 100)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *DataSourceImpl) UpdateStation(id string, m *Model) (*Model, error) {
	new := Model{
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

func (ds *DataSourceImpl) FindOneStation(id string) (*Model, error) {
	m := &Model{
		RecordID: id,
	}
	result := ds.db.Preload("IsFavorites").First(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *DataSourceImpl) FindManyAndCountStation(s string, limit int, page int) ([]*Model, int64, error) {
	offset := (page - 1) * limit

	var m []*Model

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

func (ds *DataSourceImpl) CountStation(s string) (int64, error) {
	var count int64

	result := ds.db.Model(&Model{}).Where("libelle LIKE '%'||?||'%'", s).Count(&count)
	if result.Error != nil {
		return 0, result.Error
	}

	return count, nil
}

func (ds *DataSourceImpl) CreateIsFavorite(m *isfavorite.Model) (*isfavorite.Model, error) {
	result := ds.db.Create(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (ds *DataSourceImpl) RemoveIsFavorite(m *isfavorite.Model) error {
	result := ds.db.Where("user_id = ? AND station_id = ?", m.UserID, m.StationID).Delete(&isfavorite.Model{})

	if result.Error != nil {
		return result.Error
	}
	return nil
}
