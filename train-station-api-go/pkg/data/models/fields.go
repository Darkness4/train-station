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

func NewFieldsModelFromEntity(e entities.Fields) (*FieldsModel, error) {
	geoshape, err := NewGeometryModelFromEntity(*e.GeoShape)
	if err != nil {
		return nil, err
	}
	geoPoint2D, err := converters.CoordinatesToString(e.GeoPoint2D)
	if err != nil {
		return nil, err
	}
	cGeo, err := converters.CoordinatesToString(e.CGeo)
	if err != nil {
		return nil, err
	}

	return &FieldsModel{
		Commune:    e.Commune,
		YWgs84:     e.YWgs84,
		XWgs84:     e.XWgs84,
		Libelle:    e.Libelle,
		IDGaia:     e.IDGaia,
		Voyageurs:  e.Voyageurs,
		GeoPoint2D: geoPoint2D,
		CodeLigne:  e.CodeLigne,
		XL93:       e.XL93,
		CGeo:       cGeo,
		RgTroncon:  e.RgTroncon,
		GeoShape:   geoshape,
		PK:         e.PK,
		IDreseau:   e.IDreseau,
		Departemen: e.Departemen,
		YL93:       e.YL93,
		Fret:       e.Fret,
	}, nil
}

func (m FieldsModel) Entity() (*entities.Fields, error) {
	geoshape, err := m.GeoShape.Entity()
	if err != nil {
		return nil, err
	}
	geoPoint2D, err := converters.StringToCoordinates(m.GeoPoint2D)
	if err != nil {
		return nil, err
	}
	cGeo, err := converters.StringToCoordinates(m.CGeo)
	if err != nil {
		return nil, err
	}
	return &entities.Fields{
		Commune:    m.Commune,
		YWgs84:     m.YWgs84,
		XWgs84:     m.XWgs84,
		Libelle:    m.Libelle,
		IDGaia:     m.IDGaia,
		Voyageurs:  m.Voyageurs,
		GeoPoint2D: geoPoint2D,
		CodeLigne:  m.CodeLigne,
		XL93:       m.XL93,
		CGeo:       cGeo,
		RgTroncon:  m.RgTroncon,
		GeoShape:   geoshape,
		PK:         m.PK,
		IDreseau:   m.IDreseau,
		Departemen: m.Departemen,
		YL93:       m.YL93,
		Fret:       m.Fret,
	}, nil
}
