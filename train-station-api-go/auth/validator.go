package auth

import (
	"context"
	"errors"
	"fmt"
	"net/http"

	authv1alpha1 "github.com/Darkness4/train-station-api/gen/go/auth/v1alpha1"
)

var (
	ErrUnknownProvider    = errors.New("unknown provider")
	ErrNoCredentialsGiven = errors.New("no credentials given")
)

func Validate(ctx context.Context, account *authv1alpha1.Account) (string, error) {
	if account == nil {
		return "", ErrNoCredentialsGiven
	}
	switch account.GetProvider() {
	case "github":
		req, err := http.NewRequestWithContext(ctx, "GET", "https://api.github.com/user", nil)
		if err != nil {
			return "", err
		}
		req.Header.Add("Accept", "application/vnd.github+json")
		req.Header.Add("Authorization", fmt.Sprintf("Bearer %s", account.AccessToken))
		req.Header.Add("X-GitHub-Api-Version", "2022-11-28")
		resp, err := http.DefaultClient.Do(req)
		if err != nil {
			return "", err
		}
		defer func() {
			_ = resp.Body.Close()
		}()
	default:
		return "", ErrUnknownProvider
	}

	return fmt.Sprintf("%s:%s", account.GetProvider(), account.GetProviderAccountId()), nil
}
