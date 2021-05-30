package entities

type Fields struct {
	Commune    *string   `json:"commune" validate:"required"`
	YWgs84     float64   `json:"y_wgs84" validate:"required"`
	XWgs84     float64   `json:"x_wgs84" validate:"required"`
	Libelle    string    `json:"libelle" validate:"required"`
	IDGaia     string    `json:"idgaia" validate:"required"`
	Voyageurs  string    `json:"voyageurs" validate:"required"`
	GeoPoint2D []float64 `json:"geo_point_2d" validate:"required"`
	CodeLigne  string    `json:"code_ligne" validate:"required"`
	XL93       float64   `json:"x_l93" validate:"required"`
	CGeo       []float64 `json:"c_geo" validate:"required"`
	RgTroncon  int64     `json:"rg_troncon" validate:"required"`
	GeoShape   *Geometry `json:"geo_shape" validate:"required"`
	PK         string    `json:"pk" validate:"required"`
	IDreseau   uint64    `json:"idreseau" validate:"required"`
	Departemen string    `json:"departemen" validate:"required"`
	YL93       float64   `json:"y_l93" validate:"required"`
	Fret       string    `json:"fret" validate:"required"`
}
