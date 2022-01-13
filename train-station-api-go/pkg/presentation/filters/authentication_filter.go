package filters

import (
	"strings"

	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/domain/auth"
	"github.com/savsgio/atreugo/v11"
)

type AuthenticationFilter struct {
	auth auth.Service
}

func NewAuthenticationFilter(
	auth auth.Service,
) *AuthenticationFilter {
	if auth == nil {
		internal.Logger.Panic("NewAuthenticationFilter: auth is nil")
	}
	return &AuthenticationFilter{auth}
}

// Verify if the user is authenticated
//
// If true, returns the context with the uid. If false, returns the context with an error.
func (f *AuthenticationFilter) Apply(
	ctx *atreugo.RequestCtx,
	onSuccess func(ctx *atreugo.RequestCtx, uid string) error,
) error {
	authorization := string(ctx.RequestCtx.Request.Header.Peek("Authorization"))
	idToken := strings.TrimSpace(strings.Replace(authorization, "Bearer", "", 1))
	if uid, err := f.auth.VerifyIDToken(ctx, idToken); err == nil {
		return onSuccess(ctx, uid)
	} else {
		return err
	}
}
