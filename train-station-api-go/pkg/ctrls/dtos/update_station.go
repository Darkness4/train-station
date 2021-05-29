package dtos

import "github.com/Darkness4/train-station-api/pkg/domain/entities"

type UpdateStation struct {
	DatasetID       string `json:"datasetID"`
	IsFavorite      *bool  `json:"is_favorite"`
	Libelle         string `json:"libelle"`
	RecordTimestamp string `json:"record_timestamp"`
}

func (dto UpdateStation) Entity() (entities.Station, error) {
	options := entities.Station{
		DatasetID:       dto.DatasetID,
		IsFavorite:      dto.IsFavorite,
		Libelle:         dto.Libelle,
		RecordTimestamp: dto.RecordTimestamp,
	}

	return options, nil
}
