package db

import (
	"database/sql"
	"embed"

	"github.com/Darkness4/train-station/go/logger"
	"github.com/golang-migrate/migrate/v4"
	"github.com/golang-migrate/migrate/v4/database/sqlite"
	"github.com/golang-migrate/migrate/v4/source/iofs"
	"go.uber.org/zap"
)

//go:embed migrations/*.sql
var migrations embed.FS

func InitialMigration(db *sql.DB) {
	dbDriver, err := sqlite.WithInstance(db, &sqlite.Config{
		NoTxWrap: true,
	})
	if err != nil {
		logger.I.Panic("failed to attach db", zap.Error(err))
	}
	iofsDriver, err := iofs.New(migrations, "migrations")
	if err != nil {
		logger.I.Panic("failed to load migrations", zap.Error(err))
	}
	defer iofsDriver.Close()
	m, err := migrate.NewWithInstance(
		"iofs",
		iofsDriver,
		"sqlite",
		dbDriver,
	)
	if err != nil {
		logger.I.Panic("failed to load db", zap.Error(err))
	}
	if version, dirty, err := m.Version(); err == migrate.ErrNilVersion {
		logger.I.Warn("No migrations detected. Attempting initial migration...")
		if err = m.Up(); err != nil {
			logger.I.Panic("failed to migrate db", zap.Error(err))
		}
		logger.I.Info("DB migrated.")
	} else if dirty {
		logger.I.Panic("DB is in dirty state.")
	} else if err != nil {
		logger.I.Panic("Failed to fetch DB version.", zap.Error(err))
	} else {
		logger.I.Info("DB version detected.", zap.Uint("version", version))
		if new, err := iofsDriver.Next(version); err != nil {
			logger.I.Info("Latest DB version.", zap.Uint("version", version))
		} else {
			logger.I.Warn("New DB version detected.", zap.Uint("actual", version), zap.Uint("new", new))
			if err = m.Up(); err != nil {
				logger.I.Panic("failed to migrate db", zap.Error(err))
			}
			logger.I.Info("DB migrated.")
		}
	}
}
