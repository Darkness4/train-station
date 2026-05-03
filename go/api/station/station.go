package station

import (
	"context"
	"database/sql"
	"fmt"

	"connectrpc.com/connect"
	"github.com/Darkness4/train-station/go/db"
	trainstationv1alpha1 "github.com/Darkness4/train-station/go/gen/trainstation/v1alpha1"
	"github.com/Darkness4/train-station/go/gen/trainstation/v1alpha1/trainstationv1alpha1connect"
	"github.com/Darkness4/train-station/go/introspection"
	"github.com/rs/zerolog/log"
)

var _ trainstationv1alpha1connect.StationAPIHandler = (*StationAPIHandler)(nil)

type StationAPIHandler struct {
	q *db.Queries
}

func NewAPIHandler(q *db.Queries) *StationAPIHandler {
	if q == nil {
		log.Panic().Msg("q is nil")
	}
	return &StationAPIHandler{q: q}
}

func (s *StationAPIHandler) GetManyStations(
	ctx context.Context,
	req *connect.Request[trainstationv1alpha1.GetManyStationsRequest],
) (*connect.Response[trainstationv1alpha1.GetManyStationsResponse], error) {
	userID, ok := introspection.GetSubject(ctx)
	if !ok {
		log.Panic().Msg("failed to get subject, AuthMiddleware was not called")
	}

	total, err := s.q.CountStations(ctx, req.Msg.GetQuery())
	if err != nil {
		log.Err(err).Any("req", req).Msg("count station failed")
		return nil, err
	}
	pageCount := total/req.Msg.GetLimit() + 1

	res, err := s.q.FindManyStationAndFavorite(
		ctx,
		db.FindManyStationAndFavoriteParams{
			UserID: userID,
			Search: req.Msg.GetQuery(),
			Limit:  req.Msg.GetLimit(),
			Page:   req.Msg.GetPage(),
		},
	)
	if err == sql.ErrNoRows {
		return &connect.Response[trainstationv1alpha1.GetManyStationsResponse]{
			Msg: &trainstationv1alpha1.GetManyStationsResponse{
				Stations: &trainstationv1alpha1.PaginatedStation{
					Data:      []*trainstationv1alpha1.Station{},
					Count:     0,
					Total:     total,
					Page:      req.Msg.GetPage(),
					PageCount: pageCount,
				},
			},
		}, nil
	}

	data := StationAndFavoritesFromDB(res)

	return &connect.Response[trainstationv1alpha1.GetManyStationsResponse]{
		Msg: &trainstationv1alpha1.GetManyStationsResponse{
			Stations: &trainstationv1alpha1.PaginatedStation{
				Data:      data,
				Count:     int64(len(data)),
				Total:     total,
				Page:      req.Msg.GetPage(),
				PageCount: pageCount,
			},
		},
	}, nil
}

func (s *StationAPIHandler) GetOneStation(
	ctx context.Context,
	req *connect.Request[trainstationv1alpha1.GetOneStationRequest],
) (*connect.Response[trainstationv1alpha1.GetOneStationResponse], error) {
	userID, ok := introspection.GetSubject(ctx)
	if !ok {
		log.Panic().Msg("failed to get subject, AuthMiddleware was not called")
	}

	res, err := s.q.FindOneStationAndFavorite(ctx, db.FindOneStationAndFavoriteParams{
		UserID: userID,
		ID:     req.Msg.GetId(),
	})
	if err == sql.ErrNoRows {
		return nil, connect.NewError(connect.CodeNotFound, fmt.Errorf("station not found: %w", err))
	}
	return &connect.Response[trainstationv1alpha1.GetOneStationResponse]{
		Msg: &trainstationv1alpha1.GetOneStationResponse{
			Station: StationAndFavoriteFromDB(res),
		},
	}, nil
}

func (s *StationAPIHandler) SetFavoriteOneStation(
	ctx context.Context,
	req *connect.Request[trainstationv1alpha1.SetFavoriteOneStationRequest],
) (*connect.Response[trainstationv1alpha1.SetFavoriteOneStationResponse], error) {
	userID, ok := introspection.GetSubject(ctx)
	if !ok {
		log.Panic().Msg("failed to get subject, AuthMiddleware was not called")
	}

	if req.Msg.GetValue() {
		if err := s.q.CreateFavorite(ctx, db.CreateFavoriteParams{
			StationID: req.Msg.GetId(),
			UserID:    userID,
		}); err != nil {
			return nil, err
		}

	} else {
		if err := s.q.DeleteFavorite(ctx, db.DeleteFavoriteParams{
			StationID: req.Msg.GetId(),
			UserID:    userID,
		}); err != nil {
			return nil, err
		}
	}

	return &connect.Response[trainstationv1alpha1.SetFavoriteOneStationResponse]{
		Msg: &trainstationv1alpha1.SetFavoriteOneStationResponse{},
	}, nil
}
