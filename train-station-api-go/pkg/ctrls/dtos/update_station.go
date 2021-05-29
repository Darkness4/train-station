package dtos

import "github.com/Darkness4/train-station-api/pkg/domain/entities"

type UpdateStation struct {
	DatasetID       *string `json:"datasetID"`
	Favorite        *bool   `json:"is_favorite"`
	Libelle         *string `json:"libelle"`
	RecordTimestamp *string `json:"record_timestamp"`
}

func (dto *UpdateStation) Entity() (*entities.StationOptions, error) {
	options := entities.StationOptions{
		DatasetID:       dto.DatasetID,
		Favorite:        dto.Favorite,
		Libelle:         dto.Libelle,
		RecordTimestamp: dto.RecordTimestamp,
	}

	return &options, nil
}
