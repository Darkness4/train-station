package db_test

import (
	"context"
	"database/sql"
	"fmt"
	"testing"

	"github.com/Darkness4/train-station/go/db"
	"github.com/Darkness4/train-station/go/logger"
	_ "github.com/golang-migrate/migrate/v4/source/file"
	"github.com/google/uuid"
	"github.com/stretchr/testify/suite"
	"go.uber.org/zap"
	_ "modernc.org/sqlite"
)

func fakeStation(id string) db.Station {
	return db.Station{
		ID:         id,
		Commune:    "commune",
		YWgs84:     1.0,
		XWgs84:     1.0,
		Libelle:    "libelle",
		Idgaia:     "idgaia",
		Voyageurs:  "voyageurs",
		GeoPoint2d: "geopoint2d",
		CodeLigne:  "codeligne",
		XL93:       1.0,
		CGeo:       "cgeo",
		RgTroncon:  1,
		GeoShape:   "geoshape",
		Pk:         "pk",
		Idreseau:   1,
		Departemen: "departement",
		YL93:       1.0,
		Fret:       "fret",
	}
}

type DBTestSuite struct {
	suite.Suite
	db *sql.DB
	q  *db.Queries
}

func (suite *DBTestSuite) BeforeTest(suiteName, testName string) {
	d, err := sql.Open("sqlite", fmt.Sprintf("file:%s?mode=memory&cache=shared", uuid.NewString()))
	if err != nil {
		logger.I.Panic("failed to open db", zap.Error(err))
	}
	suite.db = d
	db.InitialMigration(d)
	suite.q = db.New(suite.db)
}

func (suite *DBTestSuite) AfterTest(suiteName, testName string) {
	_ = suite.db.Close()
}

func (suite *DBTestSuite) TestCreateManyStation() {
	tests := []struct {
		input         []db.Station
		isError       bool
		errorContains []string
		expectedCount int64
		title         string
	}{
		{
			input: []db.Station{
				fakeStation("id"),
				fakeStation("id2"),
			},
			expectedCount: 2,
			title:         "Positive test",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			ctx := context.Background()

			// Act

			err := func() error {
				tx, err := suite.db.BeginTx(ctx, nil)
				if err != nil {
					return err
				}
				defer tx.Rollback()
				if err := suite.q.CreateManyStationsWithTx(ctx, suite.db, tt.input...); err != nil {
					return err
				}
				return tx.Commit()
			}()

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				count, err := suite.q.CountStations(ctx, "")
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expectedCount, count)
			}
		})
	}
}

func (suite *DBTestSuite) TestFindOneStationAndFavorite() {
	// Arrange
	ctx := context.Background()
	err := suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id2")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id3")))
	suite.Require().NoError(err)
	m := db.Favorite{
		StationID: "id2",
		UserID:    "userid",
	}
	err = suite.q.CreateFavorite(ctx, db.CreateFavoriteParams(m))
	suite.Require().NoError(err)
	m = db.Favorite{
		StationID: "id3",
		UserID:    "otheruser",
	}
	err = suite.q.CreateFavorite(ctx, db.CreateFavoriteParams(m))
	suite.Require().NoError(err)

	tests := []struct {
		input struct {
			id     string
			userID string
		}
		expected      db.FindOneStationAndFavoriteRow
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "id",
				userID: "userid",
			},
			expected: db.FindOneStationAndFavoriteRow{
				Station: fakeStation("id"),
			},
			title: "Positive test: find one with no favorite",
		},
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "id2",
				userID: "userid",
			},
			expected: db.FindOneStationAndFavoriteRow{
				Station:  fakeStation("id2"),
				Favorite: true,
			},
			title: "Positive test: find one with favorite by us",
		},
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "id3",
				userID: "userid",
			},
			expected: db.FindOneStationAndFavoriteRow{
				Station: fakeStation("id3"),
			},
			title: "Positive test: find one with favorite by someone else",
		},
		{
			input: struct {
				id     string
				userID string
			}{
				id:     "idnotexists",
				userID: "userid",
			},
			isError:       true,
			errorContains: []string{"sql: no rows in result set"},
			title:         "Negative test: find none",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			got, err := suite.q.FindOneStationAndFavorite(ctx, db.FindOneStationAndFavoriteParams{
				UserID: tt.input.userID,
				ID:     tt.input.id,
			})

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, got)
			}
		})
	}
}

func (suite *DBTestSuite) TestFindManyFindManyStationAndFavorite() {
	// Arrange
	ctx := context.Background()
	err := suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id2")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id3")))
	suite.Require().NoError(err)
	m := db.Favorite{
		StationID: "id2",
		UserID:    "userid",
	}
	err = suite.q.CreateFavorite(ctx, db.CreateFavoriteParams(m))
	suite.Require().NoError(err)
	m = db.Favorite{
		StationID: "id3",
		UserID:    "otheruser",
	}
	err = suite.q.CreateFavorite(ctx, db.CreateFavoriteParams(m))
	suite.Require().NoError(err)

	tests := []struct {
		input struct {
			userID string
			search string
			limit  int64
			page   int64
		}
		expected      []db.FindManyStationAndFavoriteRow
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: struct {
				userID string
				search string
				limit  int64
				page   int64
			}{
				userID: "userid",
				search: "",
				limit:  100,
				page:   1,
			},
			expected: []db.FindManyStationAndFavoriteRow{
				{
					Station: fakeStation("id"),
				},
				{
					Station:  fakeStation("id2"),
					Favorite: true,
				},
				{
					Station: fakeStation("id3"),
				},
			},
			title: "Positive test: find all station",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			got, err := suite.q.FindManyStationAndFavorite(
				ctx,
				db.FindManyStationAndFavoriteParams{
					UserID: tt.input.userID,
					Search: tt.input.search,
					Page:   tt.input.page,
					Limit:  tt.input.limit,
				},
			)

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, got)
			}
		})
	}
}

func (suite *DBTestSuite) TestCreateFavorite() {
	// Arrange
	ctx := context.Background()
	err := suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id2")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id3")))
	suite.Require().NoError(err)

	tests := []struct {
		input         db.Favorite
		expected      int64
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: db.Favorite{
				StationID: "id",
				UserID:    "userid",
			},
			expected: 1,
			title:    "Positive test: find one with no favorite",
		},
		{
			input: db.Favorite{
				StationID: "idnonexists",
				UserID:    "userid",
			},
			isError:       true,
			errorContains: []string{"FOREIGN KEY constraint failed"},
			title:         "Negative test: cannot create favorite on non existant station",
		},
		{
			input: db.Favorite{
				StationID: "idnonexists",
				UserID:    "",
			},
			isError:       true,
			errorContains: []string{"CHECK constraint failed: user_id <> ''"},
			title:         "Negative test: cannot create favorite on empty userID",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			err := suite.q.CreateFavorite(ctx, db.CreateFavoriteParams(tt.input))

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				count, err := suite.q.CountFavorites(ctx)
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, count)
			}
		})
	}
}

func (suite *DBTestSuite) TestDeleteFavorite() {
	// Arrange
	ctx := context.Background()
	err := suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id2")))
	suite.Require().NoError(err)
	err = suite.q.CreateStation(ctx, db.CreateStationParams(fakeStation("id3")))
	suite.Require().NoError(err)
	m := db.Favorite{
		StationID: "id2",
		UserID:    "userid",
	}
	err = suite.q.CreateFavorite(ctx, db.CreateFavoriteParams(m))
	suite.Require().NoError(err)
	m = db.Favorite{
		StationID: "id3",
		UserID:    "otheruser",
	}
	err = suite.q.CreateFavorite(ctx, db.CreateFavoriteParams(m))
	suite.Require().NoError(err)

	tests := []struct {
		input         db.Favorite
		expected      int64
		isError       bool
		errorContains []string
		title         string
	}{
		{
			input: db.Favorite{
				StationID: "idnonexists",
				UserID:    "userid",
			},
			expected: 2,
			title:    "Positive test: do nothing on unknown delete",
		},
		{
			input: db.Favorite{
				StationID: "id2",
				UserID:    "userid",
			},
			expected: 1,
			title:    "Positive test: delete one",
		},
	}

	for _, tt := range tests {
		suite.Run(tt.title, func() {
			// Act
			ctx := context.Background()
			err := suite.q.DeleteFavorite(ctx, db.DeleteFavoriteParams(tt.input))

			// Assert
			if tt.isError {
				suite.Require().Error(err)
				for _, contain := range tt.errorContains {
					suite.Require().ErrorContains(err, contain)
				}
			} else {
				suite.Require().NoError(err)
				count, err := suite.q.CountFavorites(ctx)
				suite.Require().NoError(err)
				suite.Require().EqualValues(tt.expected, count)
			}
		})
	}
}

func TestDataSourceTestSuite(t *testing.T) {
	suite.Run(t, new(DBTestSuite))
}
