package auth

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"

	"github.com/Darkness4/train-station/go/auth/github"
	authv1alpha1 "github.com/Darkness4/train-station/go/gen/go/auth/v1alpha1"
	"github.com/Darkness4/train-station/go/logger"
	"go.uber.org/zap"
)

var (
	ErrUnknownProvider    = errors.New("unknown provider")
	ErrNoCredentialsGiven = errors.New("no credentials given")
	ErrInvalidAccountID   = errors.New("invalid account id")
	ErrProviderNotOk      = errors.New("provider responded with a non-ok status code")
)

type UserInfo struct {
	UserID  string
	Name    string
	Picture string
}

func Validate(ctx context.Context, account *authv1alpha1.Account) (UserInfo, error) {
	if account == nil {
		return UserInfo{}, ErrNoCredentialsGiven
	}
	switch account.GetProvider() {
	case "github":
		req, err := http.NewRequestWithContext(ctx, "GET", "https://api.github.com/user", nil)
		if err != nil {
			return UserInfo{}, err
		}
		req.Header.Add("Accept", "application/vnd.github+json")
		req.Header.Add("Authorization", fmt.Sprintf("Bearer %s", account.AccessToken))
		req.Header.Add("X-GitHub-Api-Version", "2022-11-28")
		resp, err := http.DefaultClient.Do(req)
		if err != nil {
			return UserInfo{}, err
		}
		defer func() {
			_ = resp.Body.Close()
		}()
		if resp.StatusCode < 200 || resp.StatusCode >= 300 {
			return UserInfo{}, fmt.Errorf("%v: %d", ErrProviderNotOk, resp.StatusCode)
		}
		var u github.User
		if err := json.NewDecoder(resp.Body).Decode(&u); err != nil {
			logger.I.Panic("failed to decode a github API response", zap.Error(err))
		}
		if account.GetProviderAccountId() != strconv.FormatInt(u.ID, 10) {
			return UserInfo{}, fmt.Errorf("%v: %s", ErrInvalidAccountID, account.GetProviderAccountId())
		}

		var username = u.Name
		if username == "" {
			username = u.Login
		}

		return UserInfo{
			UserID:  fmt.Sprintf("%s:%s", account.GetProvider(), account.GetProviderAccountId()),
			Name:    username,
			Picture: u.AvatarURL,
		}, nil
	default:
		return UserInfo{}, fmt.Errorf("%v: %s", ErrUnknownProvider, account.GetProvider())
	}
}
