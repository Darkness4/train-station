package health

import (
	"context"
	"time"

	"connectrpc.com/connect"
	healthv1 "github.com/Darkness4/train-station/go/gen/grpc/health/v1"
	"github.com/Darkness4/train-station/go/gen/grpc/health/v1/healthv1connect"
	"github.com/rs/zerolog/log"
)

var _ healthv1connect.HealthHandler = (*HealthHandler)(nil)

type HealthHandler struct {
}

func New() *HealthHandler {
	return &HealthHandler{}
}

func (h *HealthHandler) Check(
	ctx context.Context,
	req *connect.Request[healthv1.HealthCheckRequest],
) (*connect.Response[healthv1.HealthCheckResponse], error) {
	return &connect.Response[healthv1.HealthCheckResponse]{
		Msg: &healthv1.HealthCheckResponse{
			Status: healthv1.HealthCheckResponse_SERVING,
		},
	}, nil
}

func (h *HealthHandler) List(
	ctx context.Context,
	req *connect.Request[healthv1.HealthListRequest],
) (*connect.Response[healthv1.HealthListResponse], error) {
	return &connect.Response[healthv1.HealthListResponse]{
		Msg: &healthv1.HealthListResponse{
			Statuses: map[string]*healthv1.HealthCheckResponse{
				"main": {
					Status: healthv1.HealthCheckResponse_SERVING,
				},
			},
		},
	}, nil
}

func (h *HealthHandler) Watch(
	ctx context.Context,
	req *connect.Request[healthv1.HealthCheckRequest],
	stream *connect.ServerStream[healthv1.HealthCheckResponse],
) error {
	ticker := time.NewTicker(10 * time.Second)

	go func(ctx context.Context) {
		for {
			if err := stream.Send(&healthv1.HealthCheckResponse{
				Status: healthv1.HealthCheckResponse_SERVING,
			}); err != nil {
				log.Err(err).Msg("healthcheck send failed")
			}

			select {
			case <-ctx.Done():
				return
			case <-ticker.C:
			}
		}
	}(ctx)

	return nil
}
