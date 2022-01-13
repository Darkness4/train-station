package dtos

type MakeFavorite struct {
	IsFavorite *bool `json:"is_favorite" validate:"required"`
}
