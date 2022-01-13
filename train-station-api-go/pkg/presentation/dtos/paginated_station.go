package dtos

import "github.com/Darkness4/train-station-api/pkg/domain/entities"

type PaginatedStation struct {
	Data      []*entities.Station `json:"data"`
	Count     int64               `json:"count"`
	Total     int64               `json:"total"`
	Page      int                 `json:"page"`
	PageCount int64               `json:"pageCount"`
}
