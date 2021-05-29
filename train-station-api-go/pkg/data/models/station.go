package models

import (
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type StationModel struct {
	RecordID        string `gorm:"primarykey"`
	DatasetID       string
	Favorite        bool
	Libelle         string
	RecordTimestamp string
}

func NewStationModelFromEntity(entity entities.Station) StationModel {
	return StationModel{
		RecordID:        entity.RecordID,
		DatasetID:       entity.DatasetID,
		Favorite:        entity.Favorite,
		Libelle:         entity.Libelle,
		RecordTimestamp: entity.RecordTimestamp,
	}
}

func (m StationModel) Entity() entities.Station {
	return entities.Station{
		RecordID:        m.RecordID,
		DatasetID:       m.DatasetID,
		Favorite:        m.Favorite,
		Libelle:         m.Libelle,
		RecordTimestamp: m.RecordTimestamp,
	}
}

func (m StationModel) Merge(o entities.StationOptions) StationModel {
	copy := m
	if o.DatasetID != nil {
		copy.DatasetID = *o.DatasetID
	}
	if o.Favorite != nil {
		copy.Favorite = *o.Favorite
	}
	if o.Libelle != nil {
		copy.Libelle = *o.Libelle
	}
	if o.RecordTimestamp != nil {
		copy.RecordTimestamp = *o.RecordTimestamp
	}
	return copy
}
