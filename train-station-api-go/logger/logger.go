package logger

import (
	"context"
	"os"

	"github.com/grpc-ecosystem/go-grpc-middleware/v2/interceptors/logging"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

var I *zap.Logger

var atom = zap.NewAtomicLevel()

func init() {
	config := zap.NewProductionEncoderConfig()
	config.TimeKey = "timestamp"
	config.EncodeTime = zapcore.ISO8601TimeEncoder
	config.EncodeLevel = zapcore.CapitalColorLevelEncoder
	config.EncodeCaller = zapcore.FullCallerEncoder
	I = zap.New(zapcore.NewCore(
		zapcore.NewConsoleEncoder(config),
		zapcore.Lock(os.Stdout),
		atom,
	))
	atom.SetLevel(zap.InfoLevel)
}

// InterceptorLogger adapts zap logger to interceptor logger.
func InterceptorLogger(l *zap.Logger) logging.Logger {
	return logging.LoggerFunc(
		func(ctx context.Context, lvl logging.Level, msg string, fields ...any) {
			f := make([]zap.Field, 0, len(fields)/2)

			for i := 0; i < len(fields); i += 2 {
				key := fields[i]
				value := fields[i+1]

				switch v := value.(type) {
				case string:
					f = append(f, zap.String(key.(string), v))
				case int:
					f = append(f, zap.Int(key.(string), v))
				case bool:
					f = append(f, zap.Bool(key.(string), v))
				default:
					f = append(f, zap.Any(key.(string), v))
				}
			}

			logger := l.WithOptions(zap.AddCallerSkip(1)).With(f...)

			switch lvl {
			case logging.LevelWarn:
				logger.Warn(msg)
			case logging.LevelError:
				logger.Error(msg)
			}
		},
	)
}

func EnableDebug() {
	atom.SetLevel(zap.DebugLevel)
}
