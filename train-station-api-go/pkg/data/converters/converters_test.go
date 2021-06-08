package converters

import (
	"fmt"
	"reflect"
	"testing"
)

func TestStringToCoordinates(t *testing.T) {
	var workingTable = []struct {
		in   string
		want []float64
	}{
		{"[1.234, 2.468]", []float64{1.234, 2.468}},
		{"[]", []float64{}},
	}

	for _, tt := range workingTable {
		t.Run(fmt.Sprintf("\"%v\" should work", tt.in), func(t *testing.T) {
			got, err := StringToCoordinates(tt.in)
			if err != nil {
				t.Errorf("StringToCoordinates(\"%v\") gave err: %v\n", tt.in, err)
			}

			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("got != expected. got: %v, want: %v\n", got, tt.want)
			}
		})
	}

	var nonWorkingTable = []struct {
		in string
	}{
		{""},
		{"haha"},
	}

	for _, tt := range nonWorkingTable {
		t.Run(fmt.Sprintf("\"%v\" shouldn't work", tt.in), func(t *testing.T) {
			got, err := StringToCoordinates(tt.in)
			if err != nil {
				t.Logf("Got expected error: %v\n", err)
				return
			}

			t.Errorf("Unexpected continuation. got: %v, want: err\n", got)
		})
	}
}

func TestCoordinatesToString(t *testing.T) {
	var workingTable = []struct {
		in   []float64
		want string
	}{
		{[]float64{1.234, 2.468}, "[1.234,2.468]"},
		{[]float64{}, "[]"},
	}

	for _, tt := range workingTable {
		t.Run(fmt.Sprintf("%v should work", tt.in), func(t *testing.T) {
			got, err := CoordinatesToString(tt.in)
			if err != nil {
				t.Errorf("CoordinatesToString(%v) gave err %v\n", tt.in, err)
			}

			if got != tt.want {
				t.Errorf("got != expected. got: %v, want: %v\n", got, tt.want)
			}
		})
	}
}
