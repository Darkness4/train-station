package models

import (
	"github.com/Darkness4/train-station-api/pkg/data/converters"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type GeometryModel struct {
	Type        string
	Coordinates string
}

func NewGeometryModelFromEntity(e *entities.Geometry) (*GeometryModel, error) {
	coordinates, err := converters.CoordinatesToString(e.Coordinates)
	if err != nil {
		return nil, err
	}
	m := GeometryModel{
		Type:        e.Type,
		Coordinates: coordinates,
	}
	return &m, nil
}

func (m GeometryModel) Entity() (*entities.Geometry, error) {
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
