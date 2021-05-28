package models

import "github.com/Darkness4/train-station-api/pkg/domain/entities"

type StationModel struct {
	RecordID        string
	DatasetID       string
	Favorite        bool
	Libelle         string
	RecordTimestamp string
}

func NewStationModelFromEntity(entity entities.Station) StationModel {
	return StationModel{
		entity.RecordID,
		entity.DatasetID,
		entity.Favorite,
		entity.Libelle,
		entity.RecordTimestamp,
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
	if o.RecordID != nil {
		copy.RecordID = *o.RecordID
	}
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

func (m StationModel) GetRecordID() string {
	return m.RecordID
}

func (m StationModel) GetDatasetID() string {
	return m.DatasetID
}

func (m StationModel) IsFavorite() bool {
	return m.Favorite
}

func (m StationModel) GetLibelle() string {
	return m.Libelle
}

func (m StationModel) GetRecordTimestamp() string {
	return m.RecordTimestamp
}
