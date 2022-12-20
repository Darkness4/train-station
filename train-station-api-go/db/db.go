package db

import (
	"context"
	"database/sql"

	"github.com/Darkness4/train-station-api/db/models"
	"github.com/Darkness4/train-station-api/db/types"
	"github.com/Darkness4/train-station-api/logger"
	"github.com/volatiletech/sqlboiler/v4/boil"
	"github.com/volatiletech/sqlboiler/v4/queries/qm"
	"go.uber.org/zap"
)

type DB struct {
	*sql.DB
}

func (db *DB) CreateManyStation(ctx context.Context, models []*models.Station) error {
	logger.I.Debug("CreateManyStation called")
	tx, err := db.BeginTx(ctx, nil)
	if err != nil {
		logger.I.Error("CreateManyStation BeginTx failed", zap.Error(err))
		return err
	}

	for _, m := range models {
		if err := m.Insert(ctx, tx, boil.Infer()); err != nil {
			logger.I.Error(
				"CreateManyStation Insert failed",
				zap.Error(err),
				zap.Any("model", m),
			)
			if err := tx.Rollback(); err != nil {
				logger.I.Error("db rollback failed", zap.Error(err))
			}
			return err
		}
	}

	return tx.Commit()
}

func (db *DB) FindOneStationAndFavorite(ctx context.Context, id string, userID string) (*types.StationAndFavorite, error) {
	logger.I.Debug(
		"FindOneStationAndFavorite called",
		zap.String("id", id),
		zap.String("userID", userID),
	)
	var s types.StationAndFavorite
	if err := models.NewQuery(
		qm.Select("stations.*, CASE WHEN favorites.user_id IS NULL THEN 0 ELSE 1 END AS favorite"),
		qm.From(models.TableNames.Stations),
		qm.LeftOuterJoin("favorites on favorites.station_id = stations.id AND favorites.user_id = ?", userID),
		qm.Where("id = ?", id),
		qm.Limit(1),
	).Bind(ctx, db, &s); err != nil {
		logger.I.Error("FindOneStationAndFavorite failed")
		return nil, err
	}
	return &s, nil
}

func (db *DB) FindManyStationAndFavorite(
	ctx context.Context,
	userID string,
	search string,
	limit int,
	page int,
) ([]*types.StationAndFavorite, error) {
	logger.I.Debug(
		"FindManyStationAndFavorite called",
		zap.String("userID", userID),
	)
	offset := (page - 1) * limit
	queries := []qm.QueryMod{
		qm.Select("stations.*, CASE WHEN favorites.user_id IS NULL THEN 0 ELSE 1 END AS favorite"),
		qm.From(models.TableNames.Stations),
		qm.LeftOuterJoin("favorites on favorites.station_id = stations.id AND favorites.user_id = ?", userID),
		qm.Limit(limit),
		qm.Offset(offset),
		qm.OrderBy("libelle"),
	}
	if search != "" {
		queries = append(queries, qm.Where("libelle LIKE '%'||?||'%'", search))
	}
	var s []*types.StationAndFavorite
	if err := models.NewQuery(queries...).Bind(ctx, db, &s); err != nil {
		logger.I.Error("FindOneStationAndFavorite failed")
		return nil, err
	}
	return s, nil
}

func (db *DB) CountStations(
	ctx context.Context,
	search string,
) (int64, error) {
	queries := []qm.QueryMod{}
	if search != "" {
		queries = append(queries, qm.Where("libelle LIKE '%'||?||'%'", search))
	}
	return models.Stations(queries...).Count(ctx, db)
}

func (db *DB) CreateFavorite(ctx context.Context, m *models.Favorite) error {
	logger.I.Debug("CreateFavorite called", zap.Any("model", m))
	return m.Insert(ctx, db, boil.Infer())
}

func (db *DB) DeleteFavorite(ctx context.Context, m *models.Favorite) error {
	logger.I.Debug("DeleteFavorite called", zap.Any("model", m))
	_, err := models.Favorites(
		qm.Where("station_id = ?", m.StationID),
		qm.And("user_id = ?", m.UserID),
	).DeleteAll(ctx, db)
	return err
}

func (db *DB) Clear(ctx context.Context) error {
	logger.I.Debug("Clear called")
	if _, err := models.Favorites().DeleteAll(ctx, db); err != nil {
		return err
	}
	if _, err := models.Stations().DeleteAll(ctx, db); err != nil {
		return err
	}
	return nil
}
