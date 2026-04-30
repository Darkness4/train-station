package introspection

import (
	"context"
	"net/http"
	"strings"

	"github.com/rs/zerolog/log"
)

type contextKey string

var (
	subjectKey contextKey = "subject"
)

// AuthMiddleware validates the token via OAuth2 introspection and injects claims into the context.
func (s *Service) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			log.Debug().Msg("Authorization header missing")
			http.Error(w, "Authorization header missing", http.StatusUnauthorized)
			return
		}

		parts := strings.Split(authHeader, " ")
		if len(parts) != 2 || !strings.EqualFold(parts[0], "Bearer") {
			log.Debug().Str("header", authHeader).Msg("Invalid authorization format")
			http.Error(w, "Invalid authorization format", http.StatusUnauthorized)
			return
		}

		token := parts[1]

		// Validate using the introspection service.
		// We pass r.Context() so that if the client cancels the request,
		// the introspection network call is also canceled.
		resp, err := s.Validate(r.Context(), token)
		if err != nil {
			log.Debug().Err(err).Msg("Introspection failed")
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// Inject the subject and optionally scopes into the context
		ctx := context.WithValue(r.Context(), subjectKey, resp.Subject)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// GetSubject retrieves the 'sub' claim from the context.
func GetSubject(ctx context.Context) (string, bool) {
	sub, ok := ctx.Value(subjectKey).(string)
	return sub, ok
}
