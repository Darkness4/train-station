package types

import (
	"github.com/Darkness4/train-station-api/db/models"
)

type StationAndFavorite struct {
	models.Station `boil:",bind"`
	Favorite       bool `boil:"favorite"`
}
