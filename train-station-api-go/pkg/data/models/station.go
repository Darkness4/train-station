package models

import (
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type StationModel struct {
	RecordID        string `gorm:"primaryKey;autoIncrement:false"`
	DatasetID       string
	IsFavorite      *bool `gorm:"default:false"`
	Libelle         string
	RecordTimestamp string
	Fields          *FieldsModel   `gorm:"embedded;embeddedPrefix:geo_shape_"`
	Geometry        *GeometryModel `gorm:"embedded;embeddedPrefix:geo_shape_"`
}

func NewStationModelFromEntity(e entities.Station) (*StationModel, error) {
	fields, err := NewFieldsModelFromEntity(*e.Fields)
	if err != nil {
		return nil, err
	}
	geometry, err := NewGeometryModelFromEntity(*e.Geometry)
	if err != nil {
		return nil, err
	}
	return &StationModel{
		RecordID:        e.RecordID,
		DatasetID:       e.DatasetID,
		IsFavorite:      e.IsFavorite,
		Libelle:         (*e.Fields).Libelle,
		RecordTimestamp: e.RecordTimestamp,
		Geometry:        geometry,
		Fields:          fields,
	}, nil
}

func (m StationModel) Entity() (*entities.Station, error) {
	fields, err := m.Fields.Entity()
	if err != nil {
		return nil, err
	}
	geometry, err := m.Geometry.Entity()
	if err != nil {
		return nil, err
	}
	return &entities.Station{
		RecordID:        m.RecordID,
		DatasetID:       m.DatasetID,
		IsFavorite:      m.IsFavorite,
		Libelle:         m.Libelle,
		RecordTimestamp: m.RecordTimestamp,
		Geometry:        geometry,
		Fields:          fields,
	}, nil
}
