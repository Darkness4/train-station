package jwt

import (
	"errors"

	"github.com/Darkness4/train-station-api/logger"
	"github.com/golang-jwt/jwt/v4"
	"go.uber.org/zap"
)

var ErrInvalidToken = errors.New("invalid JWT token")

type Validator struct {
	secret string
}

func NewValidator(secret string) *Validator {
	return &Validator{
		secret: secret,
	}
}

func (v *Validator) Validate(token string) (string, error) {
	t, err := jwt.Parse(token, func(t *jwt.Token) (interface{}, error) {
		_, ok := t.Method.(*jwt.SigningMethodHMAC)
		if !ok {
			return nil, ErrInvalidToken
		}
		return []byte(v.secret), nil
	})
	if err != nil {
		return "", err
	}
	claims, ok := t.Claims.(jwt.MapClaims)
	if ok && t.Valid {
		logger.I.Debug("received jwt token", zap.Any("claims", claims))
		userID := claims["email"].(string)
		return userID, nil
	}
	return "", ErrInvalidToken
}
