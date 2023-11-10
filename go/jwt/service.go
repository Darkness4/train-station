package jwt

import (
	"errors"
	"time"

	"github.com/Darkness4/train-station/go/logger"
	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"
)

var ErrInvalidToken = errors.New("invalid JWT token")

type Service struct {
	secret []byte
}

func New(secret []byte) *Service {
	return &Service{
		secret: secret,
	}
}

func (s *Service) CreateToken(subject string) string {
	claims := &jwt.RegisteredClaims{
		Subject:   subject,
		ExpiresAt: jwt.NewNumericDate(time.Now().Add(time.Hour * 24)),
		Issuer:    "train station API",
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS512, claims)
	signed, err := token.SignedString(s.secret)
	if err != nil {
		logger.I.Panic("failed to sign jwt", zap.Error(err))
	}
	return signed
}

func (s *Service) Validate(token string) (string, error) {
	t, err := jwt.Parse(token, func(t *jwt.Token) (interface{}, error) {
		_, ok := t.Method.(*jwt.SigningMethodHMAC)
		if !ok {
			return nil, ErrInvalidToken
		}
		return s.secret, nil
	})
	if err != nil {
		return "", err
	}
	claims, ok := t.Claims.(jwt.MapClaims)
	if ok && t.Valid {
		userID := claims["sub"].(string)
		return userID, nil
	}
	return "", ErrInvalidToken
}
