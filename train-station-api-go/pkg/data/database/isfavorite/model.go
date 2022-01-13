package isfavorite

type Model struct {
	UserID    string `gorm:"primaryKey;autoIncrement:false;check:user_id <> ''"`
	StationID string `gorm:"primaryKey;autoIncrement:false;check:station_id <> ''"`
}

func (Model) TableName() string {
	return "is_favorites"
}
