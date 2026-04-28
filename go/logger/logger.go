package logger

import (
	"context"

	"github.com/grpc-ecosystem/go-grpc-middleware/v2/interceptors/logging"
	"github.com/rs/zerolog"
)

// InterceptorLogger adapts zerolog logger to interceptor logger.
func InterceptorLogger(l zerolog.Logger) logging.Logger {
	return logging.LoggerFunc(
		func(ctx context.Context, lvl logging.Level, msg string, fields ...any) {
			var logEvent *zerolog.Event

			switch lvl {
			case logging.LevelDebug:
				logEvent = l.Debug()
			case logging.LevelInfo:
				logEvent = l.Info()
			case logging.LevelWarn:
				logEvent = l.Warn()
			case logging.LevelError:
				logEvent = l.Error()
			default:
				logEvent = l.Info()
			}

			for i := 0; i < len(fields); i += 2 {
				key := fields[i].(string)
				value := fields[i+1]
				logEvent = logEvent.Any(key, value)
			}

			logEvent.Msg(msg)
		},
	)
}
