package filters

import (
	"strings"

	"github.com/Darkness4/train-station-api/pkg/domain/services"
	"github.com/savsgio/atreugo/v11"
	"github.com/spf13/viper"
)

type AuthenticationFilter struct {
	auth services.AuthService
}

func NewAuthenticationFilter(
	auth services.AuthService,
) *AuthenticationFilter {
	if auth == nil {
		panic("NewAuthenticationFilter: auth is nil")
	}
	filter := AuthenticationFilter{auth}
	return &filter
}

// Verify if the user is authenticated
//
// If true, returns the context with the uid. If false, returns the context with an error.
func (f *AuthenticationFilter) Apply(
	ctx *atreugo.RequestCtx,
	onSuccess func(ctx *atreugo.RequestCtx, uid string) error,
) error {
	if !viper.GetBool("debug") {
		authorization := string(ctx.RequestCtx.Request.Header.Peek("Authorization"))
		idToken := strings.TrimSpace(strings.Replace(authorization, "Bearer", "", 1))
		if uid, err := f.auth.VerifyIDToken(ctx, idToken); err == nil {
			return onSuccess(ctx, uid)
		} else {
			return err
		}
	} else {
		return onSuccess(ctx, "fake_id")
	}
}
