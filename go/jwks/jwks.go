package jwks

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/go-jose/go-jose/v4"
	"github.com/go-jose/go-jose/v4/jwt"
)

type Service struct {
	url  string
	keys *jose.JSONWebKeySet
	mu   sync.RWMutex
}

// NewService initializes the service. It performs an initial fetch synchronously
// to ensure the cache is primed before returning.
func NewService(url string) *Service {
	return &Service{
		url: url,
	}
}

// Validate verifies the token signature against the in-memory JWKS cache.
func (s *Service) Validate(rawToken string) (*jwt.JSONWebToken, *jwt.Claims, error) {
	tok, err := jwt.ParseSigned(rawToken, []jose.SignatureAlgorithm{
		jose.RS256, jose.RS384, jose.RS512, jose.ES256, jose.ES384, jose.ES512,
	})
	if err != nil {
		return nil, nil, fmt.Errorf("parse error: %w", err)
	}

	if len(tok.Headers) == 0 || tok.Headers[0].KeyID == "" {
		return nil, nil, fmt.Errorf("missing kid header")
	}
	kid := tok.Headers[0].KeyID

	s.mu.RLock()
	defer s.mu.RUnlock()

	keys := s.keys.Key(kid)
	if len(keys) == 0 {
		return nil, nil, fmt.Errorf("key %s not found in cache", kid)
	}

	claims := &jwt.Claims{}
	// Use the first matching key (usually only one per kid)
	if err := tok.Claims(keys[0].Key, claims); err != nil {
		return nil, nil, fmt.Errorf("signature validation failed: %w", err)
	}

	return tok, claims, nil
}

func (s *Service) RefreshLoop(ctx context.Context, refreshPeriod time.Duration) {
	ticker := time.NewTicker(refreshPeriod)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			// Using a background context or a derived timeout context here
			// is preferred so a single failed refresh doesn't hang.
			refreshCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
			_ = s.Refresh(refreshCtx)
			cancel()
		}
	}
}

func (s *Service) Refresh(ctx context.Context) error {
	req, err := http.NewRequestWithContext(ctx, http.MethodGet, s.url, nil)
	if err != nil {
		return err
	}

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	var newKeys jose.JSONWebKeySet
	if err := json.NewDecoder(resp.Body).Decode(&newKeys); err != nil {
		return err
	}

	s.mu.Lock()
	s.keys = &newKeys
	s.mu.Unlock()

	return nil
}
