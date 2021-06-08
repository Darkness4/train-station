package models

import (
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type StationModel struct {
	RecordID        string         `gorm:"primaryKey;autoIncrement:false"`
	DatasetID       string         `gorm:"check:dataset_id <> ''"`
	IsFavorite      *bool          `gorm:"default:false"`
	Libelle         string         `gorm:"check:libelle <> ''"`
	RecordTimestamp string         `gorm:"check:record_timestamp <> ''"`
	Fields          *FieldsModel   `gorm:"embedded;embeddedPrefix:geo_shape_"`
	Geometry        *GeometryModel `gorm:"embedded;embeddedPrefix:geo_shape_"`
}

func NewStationModelFromEntity(e *entities.Station) (*StationModel, error) {
	m := &StationModel{
		RecordID:        e.RecordID,
		DatasetID:       e.DatasetID,
		IsFavorite:      e.IsFavorite,
		RecordTimestamp: e.RecordTimestamp,
	}
	if e.Fields != nil {
		if fields, err := NewFieldsModelFromEntity(e.Fields); err == nil {
			m.Fields = fields
		} else {
			return nil, err
		}

		m.Libelle = (*e.Fields).Libelle
	}
	if e.Geometry != nil {
		if geometry, err := NewGeometryModelFromEntity(e.Geometry); err == nil {
			m.Geometry = geometry
		} else {
			return nil, err
		}
	}

	return m, nil
}

func (m StationModel) Entity() (*entities.Station, error) {
	e := &entities.Station{
		RecordID:        m.RecordID,
		DatasetID:       m.DatasetID,
		IsFavorite:      m.IsFavorite,
		RecordTimestamp: m.RecordTimestamp,
		Libelle:         m.Libelle,
	}
	if m.Fields != nil {
		if fields, err := m.Fields.Entity(); err == nil {
			e.Fields = fields
		} else {
			return nil, err
		}
	}
	if m.Geometry != nil {
		if geometry, err := m.Geometry.Entity(); err == nil {
			e.Geometry = geometry
		} else {
			return nil, err
		}
	}

	return e, nil
}
