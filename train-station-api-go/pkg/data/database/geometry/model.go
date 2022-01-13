package geometry

import (
	"github.com/Darkness4/train-station-api/pkg/data/converters"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type Model struct {
	Type        string
	Coordinates string `gorm:"check:geo_shape_coordinates <> ''"`
}

func (Model) TableName() string {
	return "geometries"
}

func NewModelFromEntity(e *entities.Geometry) (*Model, error) {
	coordinates, err := converters.CoordinatesToString(e.Coordinates)
	if err != nil {
		return nil, err
	}
	m := Model{
		Type:        e.Type,
		Coordinates: coordinates,
	}
	return &m, nil
}

func (m Model) Entity() (*entities.Geometry, error) {
	coordinates, err := converters.StringToCoordinates(m.Coordinates)
	if err != nil {
		return nil, err
	}
	e := &entities.Geometry{
		Type:        m.Type,
		Coordinates: coordinates,
	}
	return e, nil
}
