package providers

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/data/database/station"
	"gorm.io/gorm"
)

func StationDataSource(db *gorm.DB) station.DataSource {
	internal.Logger.Debug("Provide StationDataSource")
	return station.NewDataSource(db)
}
