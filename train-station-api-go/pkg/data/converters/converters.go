package converters

import "encoding/json"

func StringToCoordinates(in string) ([]float64, error) {
	var out []float64
	if err := json.Unmarshal([]byte(in), &out); err != nil {
		return nil, err
	}
	return out, nil
}

func CoordinatesToString(in []float64) (string, error) {
	bytes, err := json.Marshal(in)
	if err != nil {
		return "", err
	}
	return string(bytes), nil
}
