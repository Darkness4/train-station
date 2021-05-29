package entities

type Geometry struct {
	Type        string    `json:"type" validate:"requiredOnCreate"`
	Coordinates []float64 `json:"coordinates" validate:"requiredOnCreate"`
}
