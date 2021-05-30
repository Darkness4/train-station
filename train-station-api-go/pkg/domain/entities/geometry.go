package entities

type Geometry struct {
	Type        string    `json:"type" validate:"required"`
	Coordinates []float64 `json:"coordinates" validate:"required"`
}
