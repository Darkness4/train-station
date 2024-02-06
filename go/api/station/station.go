package station

import (
	"context"
	"database/sql"

	"github.com/Darkness4/train-station/go/api/mappers"
	"github.com/Darkness4/train-station/go/db"
	trainstationv1alpha1 "github.com/Darkness4/train-station/go/gen/go/trainstation/v1alpha1"
	"github.com/Darkness4/train-station/go/jwt"
	"github.com/Darkness4/train-station/go/logger"
	"go.uber.org/zap"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type stationAPIServer struct {
	trainstationv1alpha1.UnimplementedStationAPIServer
	q   *db.Queries
	jwt *jwt.Service
}

func New(q *db.Queries, jwt *jwt.Service) *stationAPIServer {
	if q == nil {
		logger.I.Panic("q is nil")
	}
	if jwt == nil {
		logger.I.Panic("jwt is nil")
	}
	return &stationAPIServer{
		q:   q,
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

	total, err := s.q.CountStations(ctx, req.GetQuery())
	if err != nil {
		logger.I.Error("count station failed", zap.Error(err), zap.Any("req", req))
		return nil, err
	}
	pageCount := total/req.GetLimit() + 1

	res, err := s.q.FindManyStationAndFavorite(
		ctx,
		db.FindManyStationAndFavoriteParams{
			UserID: userID,
			Search: req.GetQuery(),
			Limit:  req.GetLimit(),
			Page:   req.GetPage(),
		},
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

	res, err := s.q.FindOneStationAndFavorite(ctx, db.FindOneStationAndFavoriteParams{
		UserID: userID,
		ID:     req.GetId(),
	})
	if err == sql.ErrNoRows {
		return nil, status.Error(codes.NotFound, "station not found")
	}
	return &trainstationv1alpha1.GetOneStationResponse{
		Station: mappers.StationAndFavoriteFromDB(res),
	}, nil
}

func (s *stationAPIServer) SetFavoriteOneStation(
	ctx context.Context,
	req *trainstationv1alpha1.SetFavoriteOneStationRequest,
) (*trainstationv1alpha1.SetFavoriteOneStationResponse, error) {
	userID, err := s.jwt.Validate(req.GetToken())
	if err != nil {
		logger.I.Error("authentication error", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticate: %s", err)
	}

	if req.GetValue() {
		if err := s.q.CreateFavorite(ctx, db.CreateFavoriteParams{
			StationID: req.GetId(),
			UserID:    userID,
		}); err != nil {
			return nil, err
		}

	} else {
		if err := s.q.DeleteFavorite(ctx, db.DeleteFavoriteParams{
			StationID: req.GetId(),
			UserID:    userID,
		}); err != nil {
			return nil, err
		}
	}

	return &trainstationv1alpha1.SetFavoriteOneStationResponse{}, nil
}
