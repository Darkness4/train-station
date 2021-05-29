package dtos

import "github.com/Darkness4/train-station-api/pkg/domain/entities"

type PaginatedStation struct {
	Data  []entities.Station `json:"data"`
	Count int64              `json:"count"`
	Page  int                `json:"page"`
}
