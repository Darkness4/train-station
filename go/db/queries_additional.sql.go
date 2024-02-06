package db

import (
	"context"
	"database/sql"
)

func (q *Queries) CreateManyStationsWithTx(
	ctx context.Context,
	db *sql.DB,
	stations ...Station,
) error {
	tx, err := db.BeginTx(ctx, nil)
	if err != nil {
		return err
	}
	defer tx.Rollback()
	qtx := q.WithTx(tx)
	for _, s := range stations {
		if err := qtx.CreateStation(ctx, CreateStationParams(s)); err != nil {
			return err
		}
	}
	return tx.Commit()
}

func (q *Queries) ClearWithTx(ctx context.Context, db *sql.DB) error {
	tx, err := db.BeginTx(ctx, nil)
	if err != nil {
		return err
	}
	defer tx.Rollback()
	qtx := q.WithTx(tx)
	if err := qtx.DeleteAllFavorites(ctx); err != nil {
		return err
	}
	if err := qtx.DeleteAllStations(ctx); err != nil {
		return err
	}
	return tx.Commit()
}
