package dtos

type Error struct {
	StatusCode int    `json:"statusCode"`
	Message    string `json:"message"`
}
