package auth

import (
	"context"

	"github.com/Darkness4/train-station/go/auth"
	authv1alpha1 "github.com/Darkness4/train-station/go/gen/go/auth/v1alpha1"
	"github.com/Darkness4/train-station/go/jwt"
	"github.com/rs/zerolog/log"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type authAPIServer struct {
	authv1alpha1.UnimplementedAuthAPIServer
	jwt *jwt.Service
}

func NewAPI(jwt *jwt.Service) *authAPIServer {
	if jwt == nil {
		log.Panic().Msg("jwt is nil")
	}
	return &authAPIServer{
		jwt: jwt,
	}
}

func (s *authAPIServer) GetJWT(
	ctx context.Context,
	req *authv1alpha1.GetJWTRequest,
) (*authv1alpha1.GetJWTResponse, error) {
	log.Debug().Any("req", req).Msg("GetJWT called")
	userInfo, err := auth.Validate(ctx, req.GetAccount())
	if err != nil {
		log.Err(err).Any("req", req).Msg("user failed to authenticate")
		return nil, status.Errorf(codes.Unauthenticated, "failed to authenticated: %s", err)
	}
	token := s.jwt.CreateToken(userInfo.UserID, userInfo.Name, userInfo.Picture)
	log.Debug().Any("token", token).Msg("GetJWT success")
	return &authv1alpha1.GetJWTResponse{
		Token: token,
	}, nil
}
