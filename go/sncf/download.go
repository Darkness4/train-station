package sncf

import (
	"context"
	"encoding/json"
	"net/http"
)

const url = "https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json"

func Download(ctx context.Context) ([]Station, error) {
	resp, err := http.Get(url)
	if err != nil {
		return []Station{}, err
	}
	defer resp.Body.Close()

	var stations []Station
	if err := json.NewDecoder(resp.Body).Decode(&stations); err != nil {
		return []Station{}, err
	}

	return stations, nil
}
