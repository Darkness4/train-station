package auth

import (
	"context"

	"firebase.google.com/go/v4/auth"
	"github.com/Darkness4/train-station-api/internal"
)

type Service interface {
	VerifyIDToken(ctxt context.Context, idToken string) (string, error)
}

type ServiceImpl struct {
	authClient *auth.Client
}

func NewService(authClient *auth.Client) *ServiceImpl {
	if authClient == nil {
		internal.Logger.Panic("NewAuthService: authClient is nil")
	}
	return &ServiceImpl{
		authClient: authClient,
	}
}

func (s *ServiceImpl) VerifyIDToken(ctxt context.Context, idToken string) (string, error) {
	token, err := s.authClient.VerifyIDToken(ctxt, idToken)
	if err != nil {
		return "", err
	}
	return token.UID, nil
}
