package introspection

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"strings"
	"sync"
	"time"
)

type Config struct {
	Endpoint     string
	ClientID     string
	ClientSecret string
	CacheTTL     time.Duration
	HTTPClient   *http.Client
}

type cachedResponse struct {
	resp      *IntrospectionResponse
	expiresAt time.Time
}

type IntrospectionResponse struct {
	Active   bool   `json:"active"`
	Scope    string `json:"scope,omitempty"`
	ClientID string `json:"client_id,omitempty"`
	Exp      int64  `json:"exp,omitempty"`
	Subject  string `json:"sub,omitempty"`
}

type Service struct {
	cfg   Config
	cache sync.Map
}

func NewService(cfg Config) *Service {
	if cfg.HTTPClient == nil {
		cfg.HTTPClient = &http.Client{Timeout: 10 * time.Second}
	}
	return &Service{cfg: cfg}
}

// Validate checks the token. It prioritizes the configured CacheTTL over the token's Exp.
func (s *Service) Validate(ctx context.Context, token string) (*IntrospectionResponse, error) {
	if val, ok := s.cache.Load(token); ok {
		entry := val.(cachedResponse)
		if time.Now().Before(entry.expiresAt) {
			return entry.resp, nil
		}
		s.cache.Delete(token)
	}

	resp, err := s.introspect(ctx, token)
	if err != nil {
		return nil, err
	}

	if !resp.Active {
		return nil, fmt.Errorf("token is inactive")
	}

	s.cache.Store(token, cachedResponse{
		resp:      resp,
		expiresAt: time.Now().Add(s.cfg.CacheTTL),
	})

	return resp, nil
}

func (s *Service) introspect(ctx context.Context, token string) (*IntrospectionResponse, error) {
	form := url.Values{}
	form.Set("token", token)

	req, err := http.NewRequestWithContext(
		ctx,
		http.MethodPost,
		s.cfg.Endpoint,
		strings.NewReader(form.Encode()),
	)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Content-Type", "application/x-www-form-urlencoded")
	req.SetBasicAuth(s.cfg.ClientID, s.cfg.ClientSecret)

	res, err := s.cfg.HTTPClient.Do(req)
	if err != nil {
		return nil, err
	}
	defer func() {
		_ = res.Body.Close()
	}()

	if res.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("introspection failed: %d", res.StatusCode)
	}

	var introResp IntrospectionResponse
	if err := json.NewDecoder(res.Body).Decode(&introResp); err != nil {
		return nil, err
	}

	return &introResp, nil
}
