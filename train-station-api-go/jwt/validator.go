package jwt

import (
	"errors"

	"github.com/golang-jwt/jwt/v4"
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
		_, ok := t.Method.(*jwt.SigningMethodECDSA)
		if !ok {
			return nil, ErrInvalidToken
		}
		return v.secret, nil
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
