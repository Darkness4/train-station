package mappers

import (
	"encoding/json"

	"github.com/Darkness4/train-station/go/db"
	"github.com/Darkness4/train-station/go/logger"
	"github.com/Darkness4/train-station/go/sncf"
	"go.uber.org/zap"
)

func coordinatesToString(in []float64) string {
	bytes, err := json.Marshal(in)
	if err != nil {
		logger.I.Panic("failed to convert", zap.Error(err))
	}
	return string(bytes)
}

func geoToString(in *sncf.Geo) string {
	bytes, err := json.Marshal(in)
	if err != nil {
		logger.I.Panic("failed to convert", zap.Error(err))
	}
	return string(bytes)
}

func StationsFromSNCF(ss []sncf.Station) []db.Station {
	rr := make([]db.Station, 0, len(ss))
	for _, s := range ss {
		rr = append(rr, db.Station{
			ID:         s.Recordid,
			Commune:    s.Fields.Commune,
			YWgs84:     s.Fields.YWgs84,
			XWgs84:     s.Fields.XWgs84,
			Libelle:    s.Fields.Libelle,
			Idgaia:     s.Fields.Idgaia,
			Voyageurs:  s.Fields.Voyageurs,
			GeoPoint2d: coordinatesToString(s.Fields.GeoPoint2D),
			CodeLigne:  s.Fields.CodeLigne,
			XL93:       s.Fields.XL93,
			RgTroncon:  s.Fields.RgTroncon,
			GeoShape:   geoToString(&s.Fields.GeoShape),
			Pk:         s.Fields.Pk,
			CGeo:       coordinatesToString(s.Fields.CGeo),
			Idreseau:   s.Fields.Idreseau,
			Departemen: s.Fields.Departemen,
			YL93:       s.Fields.YL93,
			Fret:       s.Fields.Fret,
		})
	}
	return rr
}
