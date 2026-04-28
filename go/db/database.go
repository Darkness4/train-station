package db

import (
	"database/sql"
	"embed"

	"github.com/golang-migrate/migrate/v4"
	"github.com/golang-migrate/migrate/v4/database/sqlite"
	"github.com/golang-migrate/migrate/v4/source/iofs"
	"github.com/rs/zerolog/log"
)

//go:embed migrations/*.sql
var migrations embed.FS

func InitialMigration(db *sql.DB) {
	dbDriver, err := sqlite.WithInstance(db, &sqlite.Config{
		NoTxWrap: true,
	})
	if err != nil {
		log.Panic().Err(err).Msg("failed to attach db")
	}
	iofsDriver, err := iofs.New(migrations, "migrations")
	if err != nil {
		log.Panic().Err(err).Msg("failed to load migrations")
	}
	defer func() {
		if err := iofsDriver.Close(); err != nil {
			log.Err(err).Msg("failed to close migration source")
		}
	}()
	m, err := migrate.NewWithInstance(
		"iofs",
		iofsDriver,
		"sqlite",
		dbDriver,
	)
	if err != nil {
		log.Panic().Err(err).Msg("failed to load db")
	}
	if version, dirty, err := m.Version(); err == migrate.ErrNilVersion {
		log.Warn().Msg("No migrations detected. Attempting initial migration...")
		if err = m.Up(); err != nil {
			log.Panic().Err(err).Msg("failed to migrate db")
		}
		log.Info().Msg("DB migrated.")
	} else if dirty {
		log.Panic().Msg("DB is in dirty state.")
	} else if err != nil {
		log.Panic().Err(err).Msg("Failed to fetch DB version.")
	} else {
		log.Info().Uint("version", version).Msg("DB version detected.")
		if new, err := iofsDriver.Next(version); err != nil {
			log.Info().Uint("version", version).Msg("Latest DB version.")
		} else {
			log.Warn().Uint("actual", version).Uint("new", new).Msg("New DB version detected.")
			if err = m.Up(); err != nil {
				log.Panic().Err(err).Msg("failed to migrate db")
			}
			log.Info().Msg("DB migrated.")
		}
	}
}
