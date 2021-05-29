package entities

type Station struct {
	RecordID        string    `json:"recordid" validate:"required"`
	DatasetID       string    `json:"datasetid" validate:"required"`
	IsFavorite      *bool     `json:"is_favorite" validate:"required"`
	Libelle         string    `json:"libelle" validate:"required"`
	RecordTimestamp string    `json:"record_timestamp" validate:"required"`
	Fields          *Fields   `json:"fields,omitempty" validate:"required"`
	Geometry        *Geometry `json:"geometry,omitempty" validate:"required"`
}
