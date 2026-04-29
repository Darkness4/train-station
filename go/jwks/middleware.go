package jwks

import (
	"context"
	"net/http"
	"strings"
)

type contextKey string

var (
	subjectKey contextKey = "subject"
)

// AuthMiddleware returns a middleware that validates the JWT using the JWKS Service.
// On success, it injects the 'sub' claim into the request context.
func (s *Service) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			http.Error(w, "Authorization header missing", http.StatusUnauthorized)
			return
		}

		// Expecting "Bearer <token>"
		parts := strings.Split(authHeader, " ")
		if len(parts) != 2 || !strings.EqualFold(parts[0], "Bearer") {
			http.Error(w, "Invalid authorization format", http.StatusUnauthorized)
			return
		}

		token := parts[1]

		// Validate using our Service
		_, claims, err := s.Validate(token)
		if err != nil {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		// Ensure the subject exists
		if claims.Subject == "" {
			http.Error(w, "Token missing subject claim", http.StatusUnauthorized)
			return
		}

		// Add subject to context using the private struct key
		ctx := context.WithValue(r.Context(), subjectKey, claims.Subject)

		// Continue with the new context
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// GetSubject is a helper function to retrieve the subject from a context.
func GetSubject(ctx context.Context) (string, bool) {
	sub, ok := ctx.Value(subjectKey).(string)
	return sub, ok
}
