package models

type IsFavoriteModel struct {
	UserID    string `gorm:"primaryKey;autoIncrement:false;check:user_id <> ''"`
	StationID string `gorm:"primaryKey;autoIncrement:false;check:station_id <> ''"`
}
