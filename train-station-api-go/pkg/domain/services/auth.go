package services

import (
	"context"

	"firebase.google.com/go/v4/auth"
)

type AuthService interface {
	VerifyIDToken(ctxt context.Context, idToken string) (string, error)
}

type AuthServiceImpl struct {
	AuthService
	authClient *auth.Client
}

func NewAuthService(authClient *auth.Client) *AuthServiceImpl {
	if authClient == nil {
		panic("NewAuthService: authClient is nil")
	}
	return &AuthServiceImpl{
		authClient: authClient,
	}
}

func (s *AuthServiceImpl) VerifyIDToken(ctxt context.Context, idToken string) (string, error) {
	token, err := s.authClient.VerifyIDToken(ctxt, idToken)
	if err != nil {
		return "", err
	}
	return token.UID, nil
}
