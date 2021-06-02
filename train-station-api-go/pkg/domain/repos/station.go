package repos

import (
	"github.com/Darkness4/train-station-api/pkg/domain/entities"
)

type StationRepository interface {
	GetManyAndCount(s string, limit int, page int) ([]*entities.Station, int64, error)
	GetOne(id string) (*entities.Station, error)
	CreateOne(station *entities.Station) (*entities.Station, error)
	CreateMany(stations []*entities.Station) ([]*entities.Station, error)
	UpdateOne(id string, station *entities.Station) (*entities.Station, error)
	Total() (int64, error)
}
