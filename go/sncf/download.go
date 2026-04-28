package sncf

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/rs/zerolog/log"
)

const url = "https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json"

func Download(ctx context.Context) ([]Station, error) {
	resp, err := http.Get(url)
	if err != nil {
		return []Station{}, err
	}
	defer func() {
		if err := resp.Body.Close(); err != nil {
			// we can't do much about it, just log it
			log.Err(err).Msg("failed to close response body")
		}
	}()

	var stations []Station
	if err := json.NewDecoder(resp.Body).Decode(&stations); err != nil {
		return []Station{}, err
	}

	return stations, nil
}
