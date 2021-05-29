package entities

type Fields struct {
	Commune    *string   `json:"commune"`
	YWgs84     float64   `json:"y_wgs84"`
	XWgs84     float64   `json:"x_wgs84"`
	Libelle    string    `json:"libelle"`
	IDGaia     string    `json:"idgaia"`
	Voyageurs  string    `json:"voyageurs"`
	GeoPoint2D []float64 `json:"geo_point_2d"`
	CodeLigne  string    `json:"code_ligne"`
	XL93       float64   `json:"x_l93"`
	CGeo       []float64 `json:"c_geo"`
	RgTroncon  int64     `json:"rg_troncon"`
	GeoShape   *Geometry `json:"geo_shape"`
	PK         string    `json:"pk"`
	IDreseau   uint64    `json:"idreseau"`
	Departemen string    `json:"departemen"`
	YL93       float64   `json:"y_l93"`
	Fret       string    `json:"fret"`
}
