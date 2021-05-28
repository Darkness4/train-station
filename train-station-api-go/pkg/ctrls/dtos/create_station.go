package dtos

import (
	"errors"
	"strings"

	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type CreateStation struct {
	RecordID        *string `json:"recordid"`
	DatasetID       *string `json:"datasetid"`
	Favorite        *bool   `json:"is_favorite"`
	Libelle         *string `json:"libelle"`
	RecordTimestamp *string `json:"record_timestamp"`
}

func (dto *CreateStation) Entity() (*entities.Station, error) {
	var errsb []string
	if dto.RecordID == nil {
		errsb = append(errsb, "The field recordid is null.")
	}
	if dto.DatasetID == nil {
		errsb = append(errsb, "The field datasetid is null.")
	}
	if dto.Favorite == nil {
		errsb = append(errsb, "The field is_favorite is null.")
	}
	if dto.Libelle == nil {
		errsb = append(errsb, "The field libelle is null.")
	}
	if dto.RecordTimestamp == nil {
		errsb = append(errsb, "The field record_timestamp is null.")
	}
	if len(errsb) > 0 {
		err := strings.Join(errsb, " ")
		return nil, errors.New(err)
	}

	station := entities.Station{
		RecordID:        *dto.RecordID,
		DatasetID:       *dto.DatasetID,
		Favorite:        *dto.Favorite,
		Libelle:         *dto.Libelle,
		RecordTimestamp: *dto.RecordTimestamp,
	}

	return &station, nil
}
