package entities

type Station struct {
	RecordID        string    `json:"recordid"`
	DatasetID       string    `json:"datasetid"`
	IsFavorite      *bool     `json:"is_favorite"`
	Libelle         string    `json:"libelle"`
	RecordTimestamp string    `json:"record_timestamp"`
	Fields          *Fields   `json:"fields"`
	Geometry        *Geometry `json:"geometry"`
}

// TODO: Validate
