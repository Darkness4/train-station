package sncf

type Station struct {
	Datasetid       string `json:"datasetid"`
	Recordid        string `json:"recordid"`
	Fields          Fields `json:"fields"`
	Geometry        Geo    `json:"geometry"`
	RecordTimestamp string `json:"record_timestamp"`
}

type Fields struct {
	Idreseau   int64     `json:"idreseau"`
	YL93       float64   `json:"y_l93"`
	Commune    string    `json:"commune"`
	Voyageurs  string    `json:"voyageurs"`
	CodeUic    string    `json:"code_uic"`
	GeoShape   Geo       `json:"geo_shape"`
	Fret       string    `json:"fret"`
	Libelle    string    `json:"libelle"`
	Pk         string    `json:"pk"`
	Idgaia     string    `json:"idgaia"`
	Departemen string    `json:"departemen"`
	RgTroncon  int64     `json:"rg_troncon"`
	XL93       float64   `json:"x_l93"`
	XWgs84     float64   `json:"x_wgs84"`
	CodeLigne  string    `json:"code_ligne"`
	CGeo       []float64 `json:"c_geo"`
	GeoPoint2D []float64 `json:"geo_point_2d"`
	YWgs84     float64   `json:"y_wgs84"`
}

type Geo struct {
	Coordinates []float64 `json:"coordinates"`
	Type        string    `json:"type"`
}
