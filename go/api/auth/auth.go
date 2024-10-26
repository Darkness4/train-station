package auth

import (
	"context"

	"github.com/Darkness4/train-station/go/auth"
	authv1alpha1 "github.com/Darkness4/train-station/go/gen/go/auth/v1alpha1"
	"github.com/Darkness4/train-station/go/jwt"
	"github.com/Darkness4/train-station/go/logger"
	"go.uber.org/zap"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type authAPIServer struct {
	authv1alpha1.UnimplementedAuthAPIServer
	jwt *jwt.Service
}

func NewAPI(jwt *jwt.Service) *authAPIServer {
	if jwt == nil {
		logger.I.Panic("jwt is nil")
	}
	return &authAPIServer{
		jwt: jwt,
	}
}

func (s *authAPIServer) GetJWT(
	ctx context.Context,
	req *authv1alpha1.GetJWTRequest,
) (*authv1alpha1.GetJWTResponse, error) {
	logger.I.Debug("GetJWT called", zap.Any("req", req))
	userInfo, err := auth.Validate(ctx, req.GetAccount())
	if err != nil {
		logger.I.Error("user failed to authenticate", zap.Error(err), zap.Any("req", req))
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticated: %s", err)
	}
	token := s.jwt.CreateToken(userInfo.UserID, userInfo.Name, userInfo.Picture)
	logger.I.Debug("GetJWT success", zap.Any("token", token))
	return &authv1alpha1.GetJWTResponse{
		Token: s.jwt.CreateToken(userInfo.UserID, userInfo.Name, userInfo.Picture),
	}, nil
}
