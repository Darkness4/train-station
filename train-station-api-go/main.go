package main

import (
	"fmt"
	"os"

	"github.com/Darkness4/train-station-api/cmd"
)

func main() {
	if err := cmd.Execute(); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
}
