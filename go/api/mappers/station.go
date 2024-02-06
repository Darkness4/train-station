package mappers

import (
	"encoding/json"

	"github.com/Darkness4/train-station/go/db"
	trainstationv1alpha1 "github.com/Darkness4/train-station/go/gen/go/trainstation/v1alpha1"
	"github.com/Darkness4/train-station/go/logger"
	"go.uber.org/zap"
)

func stringToCoordinates(in string) []float64 {
	var out []float64
	if err := json.Unmarshal([]byte(in), &out); err != nil {
		logger.I.Panic("failed to convert", zap.Error(err))
	}
	return out
}

func stringToGeo(in string) *trainstationv1alpha1.Geometry {
	var out trainstationv1alpha1.Geometry
	if err := json.Unmarshal([]byte(in), &out); err != nil {
		logger.I.Panic("failed to convert", zap.Error(err))
	}
	return &out
}

func StationAndFavoritesFromDB(
	ss []db.FindManyStationAndFavoriteRow,
) []*trainstationv1alpha1.Station {
	rr := make([]*trainstationv1alpha1.Station, 0, len(ss))
	for _, s := range ss {
		rr = append(rr, StationAndFavoriteFromDB(db.FindOneStationAndFavoriteRow(s)))
	}
	return rr
}

func StationAndFavoriteFromDB(s db.FindOneStationAndFavoriteRow) *trainstationv1alpha1.Station {
	return &trainstationv1alpha1.Station{
		Id:          s.Station.ID,
		Commune:     s.Station.Commune,
		YWgs84:      s.Station.YWgs84,
		XWgs84:      s.Station.XWgs84,
		Libelle:     s.Station.Libelle,
		Idgaia:      s.Station.Idgaia,
		Voyageurs:   s.Station.Voyageurs,
		GeoPoint_2D: stringToCoordinates(s.Station.GeoPoint2d),
		CodeLigne:   s.Station.CodeLigne,
		XL93:        s.Station.XL93,
		CGeo:        stringToCoordinates(s.Station.CGeo),
		RgTroncon:   s.Station.RgTroncon,
		GeoShape:    stringToGeo(s.Station.GeoShape),
		Pk:          s.Station.Pk,
		Idreseau:    s.Station.Idreseau,
		Departemen:  s.Station.Departemen,
		YL93:        s.Station.YL93,
		Fret:        s.Station.Fret,
		IsFavorite:  s.Favorite,
	}
}
