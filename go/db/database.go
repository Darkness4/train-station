package db

import (
	"database/sql"
	"embed"
	"fmt"
	"io/fs"
	"strings"

	_ "embed"
)

//go:embed migrations/*.sql
var migrationFiles embed.FS

// Migrate handles the migration logic. Direction should be "up" or "down".
func Migrate(db *sql.DB, direction string) error {
	// 1. Ensure the migration tracking table exists
	_, err := db.Exec(`
		CREATE TABLE IF NOT EXISTS schema_migrations (
			version INTEGER PRIMARY KEY,
			name TEXT NOT NULL
		);
	`)
	if err != nil {
		return fmt.Errorf("could not ensure migration table: %w", err)
	}

	// 2. Get applied versions
	rows, err := db.Query("SELECT version FROM schema_migrations ORDER BY version ASC")
	if err != nil {
		return err
	}
	applied := make(map[int]bool)
	var maxVersion int
	for rows.Next() {
		var v int
		if err = rows.Scan(&v); err != nil {
			return fmt.Errorf("failed to scan row: %w", err)
		}
		applied[v] = true
		maxVersion = v
	}
	if err = rows.Close(); err != nil {
		return fmt.Errorf("failed to close rows query: %w", err)
	}

	// 3. Load migration files
	entries, err := migrationFiles.ReadDir("migrations")
	if err != nil {
		return fmt.Errorf("failed to read migration files: %w", err)
	}

	switch direction {
	case "up":
		return migrateUp(db, entries, applied)
	case "down":
		return migrateDown(db, entries, maxVersion)
	}

	return fmt.Errorf("invalid direction: %s", direction)
}

func migrateUp(db *sql.DB, entries []fs.DirEntry, applied map[int]bool) error {
	for i, entry := range entries {
		version := i + 1 // Assuming files are named 001, 002...
		if !strings.HasSuffix(entry.Name(), ".up.sql") || applied[version] {
			continue
		}

		if err := executeFile(db, entry.Name(), version, true); err != nil {
			return err
		}
		fmt.Printf("Applied migration: %s\n", entry.Name())
	}
	return nil
}

func migrateDown(db *sql.DB, entries []fs.DirEntry, currentVersion int) error {
	if currentVersion == 0 {
		fmt.Println("No migrations to roll back.")
		return nil
	}

	// Find the specific file for the current version
	var targetFile string
	for _, entry := range entries {
		prefix := fmt.Sprintf("%03d", currentVersion)
		if strings.HasPrefix(entry.Name(), prefix) && strings.HasSuffix(entry.Name(), ".down.sql") {
			targetFile = entry.Name()
			break
		}
	}

	if targetFile == "" {
		return fmt.Errorf("down migration file for version %d not found", currentVersion)
	}

	if err := executeFile(db, targetFile, currentVersion, false); err != nil {
		return err
	}
	fmt.Printf("Rolled back migration: %s\n", targetFile)
	return nil
}

func executeFile(db *sql.DB, filename string, version int, isUp bool) error {
	content, err := migrationFiles.ReadFile("migrations/" + filename)
	if err != nil {
		return err
	}

	// Since your SQL has BEGIN/COMMIT, we execute directly on db.
	if _, err := db.Exec(string(content)); err != nil {
		// Attempt a rollback in case the script's internal transaction is dangling
		_, _ = db.Exec("ROLLBACK;")
		return fmt.Errorf("executing %s: %w", filename, err)
	}

	// Update tracking table
	if isUp {
		_, err = db.Exec(
			"INSERT INTO schema_migrations (version, name) VALUES (?, ?)",
			version,
			filename,
		)
	} else {
		_, err = db.Exec("DELETE FROM schema_migrations WHERE version = ?", version)
	}

	return fmt.Errorf("failed to update migration tracking table: %w", err)
}
