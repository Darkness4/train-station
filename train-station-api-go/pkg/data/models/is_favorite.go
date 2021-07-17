package models

type IsFavoriteModel struct {
	UserID    string        `gorm:"primaryKey;autoIncrement:false;user_id:station_id <> ''"`
	StationID string        `gorm:"check:station_id <> ''"`
	Station   *StationModel `gorm:"primaryKey;autoIncrement:false;foreignKey:StationID;references:RecordID"`
}
