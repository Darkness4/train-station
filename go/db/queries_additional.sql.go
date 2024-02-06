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
	for _, s := range stations {
		if err := q.CreateStation(ctx, CreateStationParams(s)); err != nil {
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
	if err := q.DeleteAllFavorites(ctx); err != nil {
		return err
	}
	if err := q.DeleteAllStations(ctx); err != nil {
		return err
	}
	return tx.Commit()
}
