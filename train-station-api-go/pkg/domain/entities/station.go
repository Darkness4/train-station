package entities

type Station struct {
	RecordID        string    `json:"recordid" validate:"requiredOnCreate"`
	DatasetID       string    `json:"datasetid" validate:"requiredOnCreate"`
	IsFavorite      *bool     `json:"is_favorite" validate:"requiredOnCreate"`
	Libelle         string    `json:"libelle" validate:"requiredOnCreate"`
	RecordTimestamp string    `json:"record_timestamp" validate:"requiredOnCreate"`
	Fields          *Fields   `json:"fields,omitempty" validate:"requiredOnCreate"`
	Geometry        *Geometry `json:"geometry,omitempty" validate:"requiredOnCreate"`
}
