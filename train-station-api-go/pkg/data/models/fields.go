package models

import (
	"github.com/Darkness4/train-station-api/pkg/data/converters"
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type FieldsModel struct {
	Commune    *string
	YWgs84     float64
	XWgs84     float64
	Libelle    string
	IDGaia     string
	Voyageurs  string
	GeoPoint2D string
	CodeLigne  string
	XL93       float64
	CGeo       string
	RgTroncon  int64
	GeoShape   *GeometryModel `gorm:"embedded;embeddedPrefix:geo_shape_"`
	PK         string
	IDreseau   uint64
	Departemen string
	YL93       float64
	Fret       string
}

func NewFieldsModelFromEntity(e *entities.Fields) (*FieldsModel, error) {
	m := &FieldsModel{
		Commune:    e.Commune,
		YWgs84:     e.YWgs84,
		XWgs84:     e.XWgs84,
		Libelle:    e.Libelle,
		IDGaia:     e.IDGaia,
		Voyageurs:  e.Voyageurs,
		CodeLigne:  e.CodeLigne,
		XL93:       e.XL93,
		RgTroncon:  e.RgTroncon,
		PK:         e.PK,
		IDreseau:   e.IDreseau,
		Departemen: e.Departemen,
		YL93:       e.YL93,
		Fret:       e.Fret,
	}
	if e.GeoShape != nil {
		if geoShape, err := NewGeometryModelFromEntity(e.GeoShape); err == nil {
			m.GeoShape = geoShape
		} else {
			return nil, err
		}
	}
	if geoPoint2D, err := converters.CoordinatesToString(e.GeoPoint2D); err == nil {
		m.GeoPoint2D = geoPoint2D
	} else {
		return nil, err
	}
	if cGeo, err := converters.CoordinatesToString(e.CGeo); err == nil {
		m.CGeo = cGeo
	} else {
		return nil, err
	}

	return m, nil
}

func (m FieldsModel) Entity() (*entities.Fields, error) {
	e := &entities.Fields{
		Commune:    m.Commune,
		YWgs84:     m.YWgs84,
		XWgs84:     m.XWgs84,
		Libelle:    m.Libelle,
		IDGaia:     m.IDGaia,
		Voyageurs:  m.Voyageurs,
		CodeLigne:  m.CodeLigne,
		XL93:       m.XL93,
		RgTroncon:  m.RgTroncon,
		PK:         m.PK,
		IDreseau:   m.IDreseau,
		Departemen: m.Departemen,
		YL93:       m.YL93,
		Fret:       m.Fret,
	}
	if m.GeoShape != nil {
		if geoShape, err := m.GeoShape.Entity(); err == nil {
			e.GeoShape = geoShape
		} else {
			return nil, err
		}
	}
	if geoPoint2D, err := converters.StringToCoordinates(m.GeoPoint2D); err == nil {
		e.GeoPoint2D = geoPoint2D
	} else {
		return nil, err
	}
	if cGeo, err := converters.StringToCoordinates(m.CGeo); err == nil {
		e.CGeo = cGeo
	} else {
		return nil, err
	}

	return e, nil
}
