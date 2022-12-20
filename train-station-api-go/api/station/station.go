package station

import (
	"context"
	"database/sql"

	"github.com/Darkness4/train-station-api/api/mappers"
	"github.com/Darkness4/train-station-api/db"
	"github.com/Darkness4/train-station-api/db/models"
	trainstationv1alpha1 "github.com/Darkness4/train-station-api/gen/go/trainstation/v1alpha1"
	"github.com/Darkness4/train-station-api/jwt"
	"github.com/Darkness4/train-station-api/logger"
	"go.uber.org/zap"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type stationAPIServer struct {
	trainstationv1alpha1.UnimplementedStationAPIServer
	db  *sql.DB
	jwt *jwt.Service
}

func New(db *sql.DB, jwt *jwt.Service) *stationAPIServer {
	if db == nil {
		logger.I.Panic("db is nil")
	}
	if jwt == nil {
		logger.I.Panic("jwt is nil")
	}
	return &stationAPIServer{
		db:  db,
		jwt: jwt,
	}
}

func (s *stationAPIServer) GetManyStations(ctx context.Context, req *trainstationv1alpha1.GetManyStationsRequest) (*trainstationv1alpha1.GetManyStationsResponse, error) {
	userID, err := s.jwt.Validate(req.GetToken())
	if err != nil {
		logger.I.Error("authentication error", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticate: %s", err)
	}

	total, err := db.CountStations(ctx, s.db, req.GetQuery())
	if err != nil {
		logger.I.Error("count station failed", zap.Error(err), zap.Any("req", req))
		return nil, err
	}
	pageCount := total/req.GetLimit() + 1

	res, err := db.FindManyStationAndFavorite(ctx, s.db, userID, req.GetQuery(), int(req.GetLimit()), int(req.GetPage()))
	if err == sql.ErrNoRows {
		return &trainstationv1alpha1.GetManyStationsResponse{
			Stations: &trainstationv1alpha1.PaginatedStation{
				Data:      []*trainstationv1alpha1.Station{},
				Count:     0,
				Total:     total,
				Page:      req.GetPage(),
				PageCount: pageCount,
			},
		}, nil
	}

	data := mappers.StationAndFavoritesFromDB(res)

	return &trainstationv1alpha1.GetManyStationsResponse{
		Stations: &trainstationv1alpha1.PaginatedStation{
			Data:      data,
			Count:     int64(len(data)),
			Total:     total,
			Page:      req.GetPage(),
			PageCount: pageCount,
		},
	}, nil
}
func (s *stationAPIServer) GetOneStation(ctx context.Context, req *trainstationv1alpha1.GetOneStationRequest) (*trainstationv1alpha1.GetOneStationResponse, error) {
	userID, err := s.jwt.Validate(req.GetToken())
	if err != nil {
		logger.I.Error("authentication error", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticate: %s", err)
	}

	res, err := db.FindOneStationAndFavorite(ctx, s.db, req.GetId(), userID)
	if err == sql.ErrNoRows {
		return nil, status.Error(codes.NotFound, "station not found")
	}
	return &trainstationv1alpha1.GetOneStationResponse{
		Station: mappers.StationAndFavoriteFromDB(res),
	}, nil
}
func (s *stationAPIServer) SetFavoriteOneStation(ctx context.Context, req *trainstationv1alpha1.SetFavoriteOneStationRequest) (*trainstationv1alpha1.SetFavoriteOneStationResponse, error) {
	userID, err := s.jwt.Validate(req.GetToken())
	if err != nil {
		logger.I.Error("authentication error", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticate: %s", err)
	}

	var fn func(ctx context.Context, db *sql.DB, m *models.Favorite) error
	if req.GetValue() {
		fn = db.CreateFavorite
	} else {
		fn = db.DeleteFavorite
	}

	if err := fn(ctx, s.db, &models.Favorite{
		StationID: req.Id,
		UserID:    userID,
	}); err != nil {
		return nil, err
	}

	return &trainstationv1alpha1.SetFavoriteOneStationResponse{}, nil
}
