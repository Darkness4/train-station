package entities

type Station struct {
	RecordID        string `json:"recordid"`
	DatasetID       string `json:"datasetid"`
	Favorite        bool   `json:"is_favorite"`
	Libelle         string `json:"libelle"`
	RecordTimestamp string `json:"record_timestamp"`
}

// TODO: Validate
