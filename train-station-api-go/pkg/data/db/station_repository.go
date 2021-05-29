package db

import (
	"fmt"

	"github.com/Darkness4/train-station-api/pkg/data/models"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
	"gorm.io/gorm"
)

type StationRepository struct {
	db *gorm.DB
}

func NewStationRepository(db *gorm.DB) *StationRepository {
	if db == nil {
		panic("StationRepository: db is nil")
	}
	db.AutoMigrate(&models.StationModel{})
	return &StationRepository{db}
}

func (repo *StationRepository) Create(m *models.StationModel) (*models.StationModel, error) {
	result := repo.db.Create(m)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (repo *StationRepository) Update(id string, station entities.Station) (*models.StationModel, error) {
	m := &models.StationModel{
		RecordID: id,
	}
	result := repo.db.Model(m).Updates(station)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (repo *StationRepository) FindOne(id string) (*models.StationModel, error) {
	m := &models.StationModel{}
	result := repo.db.First(m, id)

	if result.Error != nil {
		return nil, result.Error
	}
	return m, nil
}

func (repo *StationRepository) FindManyAndCount(s string, limit int, page int) ([]models.StationModel, int64, error) {
	offset := (page - 1) * limit

	var m []models.StationModel

	result := repo.db.Limit(limit).Offset(offset).Where("libelle LIKE ?", fmt.Sprintf("%%%s%%", s)).Find(&m)

	if result.Error != nil {
		return nil, result.RowsAffected, result.Error
	}
	return m, result.RowsAffected, nil
}
