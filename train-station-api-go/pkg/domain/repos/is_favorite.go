package repos

import "github.com/Darkness4/train-station-api/pkg/domain/entities"

type IsFavoriteRepository interface {
	CreateOne(station *entities.Station, userId string) error
}
