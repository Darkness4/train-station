package station

import (
	"github.com/Darkness4/train-station-api/pkg/data/database/fields"
	"github.com/Darkness4/train-station-api/pkg/data/database/geometry"
	"github.com/Darkness4/train-station-api/pkg/data/database/isfavorite"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type Model struct {
	RecordID        string              `gorm:"primaryKey;autoIncrement:false"`
	DatasetID       string              `gorm:"check:dataset_id <> ''"`
	Libelle         string              `gorm:"check:libelle <> ''"`
	RecordTimestamp string              `gorm:"check:record_timestamp <> ''"`
	Fields          *fields.Model       `gorm:"embedded;embeddedPrefix:geo_shape_"`
	Geometry        *geometry.Model     `gorm:"embedded;embeddedPrefix:geo_shape_"`
	IsFavorites     []*isfavorite.Model `gorm:"foreignKey:StationID;references:RecordID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE"`
}

func NewModelFromEntity(e *entities.Station) (*Model, error) {
	m := &Model{
		RecordID:        e.RecordID,
		DatasetID:       e.DatasetID,
		RecordTimestamp: e.RecordTimestamp,
	}
	if e.Fields != nil {
		if fields, err := fields.NewModelFromEntity(e.Fields); err == nil {
			m.Fields = fields
		} else {
			return nil, err
		}

		m.Libelle = (*e.Fields).Libelle
	}
	if e.Geometry != nil {
		if geometry, err := geometry.NewModelFromEntity(e.Geometry); err == nil {
			m.Geometry = geometry
		} else {
			return nil, err
		}
	}

	return m, nil
}

func (m Model) Entity(userId string) (*entities.Station, error) {
	isFavorite := false
	for _, favorite := range m.IsFavorites {
		if favorite.UserID == userId {
			isFavorite = true
			break
		}
	}

	e := &entities.Station{
		RecordID:        m.RecordID,
		DatasetID:       m.DatasetID,
		IsFavorite:      &isFavorite,
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
