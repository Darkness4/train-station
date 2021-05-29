package models

import (
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type StationModel struct {
	RecordID        string `gorm:"primarykey"`
	DatasetID       string
	IsFavorite      *bool
	Libelle         string
	RecordTimestamp string
}

func NewStationModelFromEntity(entity entities.Station) StationModel {
	model := StationModel{
		RecordID:        entity.RecordID,
		DatasetID:       entity.DatasetID,
		IsFavorite:      entity.IsFavorite,
		Libelle:         entity.Libelle,
		RecordTimestamp: entity.RecordTimestamp,
	}
	return model
}

func (m StationModel) Entity() entities.Station {
	return entities.Station{
		RecordID:        m.RecordID,
		DatasetID:       m.DatasetID,
		IsFavorite:      m.IsFavorite,
		Libelle:         m.Libelle,
		RecordTimestamp: m.RecordTimestamp,
	}
}
