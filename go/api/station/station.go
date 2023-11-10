package station

import (
	"context"
	"database/sql"

	"github.com/Darkness4/train-station/go/api/mappers"
	"github.com/Darkness4/train-station/go/db"
	"github.com/Darkness4/train-station/go/db/models"
	trainstationv1alpha1 "github.com/Darkness4/train-station/go/gen/go/trainstation/v1alpha1"
	"github.com/Darkness4/train-station/go/jwt"
	"github.com/Darkness4/train-station/go/logger"
	"go.uber.org/zap"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type stationAPIServer struct {
	trainstationv1alpha1.UnimplementedStationAPIServer
	db  *db.DB
	jwt *jwt.Service
}

func New(db *db.DB, jwt *jwt.Service) *stationAPIServer {
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

func (s *stationAPIServer) GetManyStations(
	ctx context.Context,
	req *trainstationv1alpha1.GetManyStationsRequest,
) (*trainstationv1alpha1.GetManyStationsResponse, error) {
	userID, err := s.jwt.Validate(req.GetToken())
	if err != nil {
		logger.I.Error("authentication error", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticate: %s", err)
	}

	total, err := s.db.CountStations(ctx, req.GetQuery())
	if err != nil {
		logger.I.Error("count station failed", zap.Error(err), zap.Any("req", req))
		return nil, err
	}
	pageCount := total/req.GetLimit() + 1

	res, err := s.db.FindManyStationAndFavorite(
		ctx,
		userID,
		req.GetQuery(),
		int(req.GetLimit()),
		int(req.GetPage()),
	)
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

func (s *stationAPIServer) GetOneStation(
	ctx context.Context,
	req *trainstationv1alpha1.GetOneStationRequest,
) (*trainstationv1alpha1.GetOneStationResponse, error) {
	userID, err := s.jwt.Validate(req.GetToken())
	if err != nil {
		logger.I.Error("authentication error", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticate: %s", err)
	}

	res, err := s.db.FindOneStationAndFavorite(ctx, req.GetId(), userID)
	if err == sql.ErrNoRows {
		return nil, status.Error(codes.NotFound, "station not found")
	}
	return &trainstationv1alpha1.GetOneStationResponse{
		Station: mappers.StationAndFavoriteFromDB(res),
	}, nil
}

type favoriteSetter func(ctx context.Context, m *models.Favorite) error

func (s *stationAPIServer) SetFavoriteOneStation(
	ctx context.Context,
	req *trainstationv1alpha1.SetFavoriteOneStationRequest,
) (*trainstationv1alpha1.SetFavoriteOneStationResponse, error) {
	userID, err := s.jwt.Validate(req.GetToken())
	if err != nil {
		logger.I.Error("authentication error", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticate: %s", err)
	}

	var fn favoriteSetter
	if req.GetValue() {
		fn = s.db.CreateFavorite
	} else {
		fn = s.db.DeleteFavorite
	}

	if err := fn(ctx, &models.Favorite{
		StationID: req.Id,
		UserID:    userID,
	}); err != nil {
		return nil, err
	}

	return &trainstationv1alpha1.SetFavoriteOneStationResponse{}, nil
}
