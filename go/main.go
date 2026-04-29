package main

import (
	"context"
	"database/sql"
	"fmt"
	"net"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/Darkness4/train-station/go/api/health"
	"github.com/Darkness4/train-station/go/api/station"
	"github.com/Darkness4/train-station/go/db"
	"github.com/Darkness4/train-station/go/db/mappers"
	"github.com/Darkness4/train-station/go/gen/grpc/health/v1/healthv1connect"
	"github.com/Darkness4/train-station/go/gen/trainstation/v1alpha1/trainstationv1alpha1connect"
	"github.com/Darkness4/train-station/go/jwks"
	"github.com/Darkness4/train-station/go/sncf"
	internaltls "github.com/Darkness4/train-station/go/tls"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/joho/godotenv"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"github.com/rs/zerolog/pkgerrors"
	"github.com/urfave/cli/v3"

	godeltaprof "github.com/grafana/pyroscope-go/godeltaprof/http/pprof"

	_ "modernc.org/sqlite"
)

const (
	_shutdownPeriod      = 10 * time.Second
	_shutdownHardPeriod  = 3 * time.Second
	_readinessDrainDelay = 5 * time.Second
)

var (
	listenAddress string

	keyFile      string
	certFile     string
	clientCAFile string

	dbFile string

	jwksURL           string
	jwksRefreshPeriod time.Duration

	version string
)

var flags = []cli.Flag{
	&cli.StringFlag{
		Name:        "grpc.listen-address",
		Value:       ":3000",
		Usage:       "Address to listen on. Is used for receiving job status via the job completion plugin.",
		Destination: &listenAddress,
		Sources:     cli.NewValueSourceChain(cli.EnvVar("LISTEN_ADDRESS")),
	},
	&cli.StringFlag{
		Name:        "tls.key-file",
		Value:       "",
		Destination: &keyFile,
		Usage:       "TLS Private Key file.",
		Sources:     cli.NewValueSourceChain(cli.EnvVar("TLS_KEY")),
	},
	&cli.StringFlag{
		Name:        "tls.cert-file",
		Value:       "",
		Destination: &certFile,
		Usage:       "TLS Certificate file.",
		Sources:     cli.NewValueSourceChain(cli.EnvVar("TLS_CERT")),
	},
	&cli.StringFlag{
		Name:        "tls.client-ca-file",
		Value:       "",
		Destination: &clientCAFile,
		Usage:       "TLS CA file to check the client certificates.",
		Sources:     cli.NewValueSourceChain(cli.EnvVar("TLS_CLIENT_CA")),
	},
	&cli.StringFlag{
		Name:        "db.path",
		Value:       "./db.sqlite3",
		Destination: &dbFile,
		Usage:       "SQLite3 database file path.",
		Sources:     cli.NewValueSourceChain(cli.EnvVar("DB_PATH")),
	},
	&cli.StringFlag{
		Name:        "jwks.url",
		Required:    true,
		Usage:       "JWKS URL used to validate incomming JWTs.",
		Destination: &jwksURL,
		Sources:     cli.NewValueSourceChain(cli.EnvVar("JWKS_URL")),
	},
	&cli.DurationFlag{
		Name:        "jwks.refresh-period",
		Value:       time.Hour,
		Usage:       "JWKS refresh period.",
		Destination: &jwksRefreshPeriod,
		Sources:     cli.NewValueSourceChain(cli.EnvVar("JWKS_REFRESH_PERIOD")),
	},
}

var app = &cli.Command{
	Name:    "train-station-api",
	Usage:   "The train station API",
	Flags:   flags,
	Version: version,
	Suggest: true,
	Action: func(ctx context.Context, c *cli.Command) error {
		ctx = log.Logger.WithContext(ctx)

		// Graceful shutdown
		ctx, stop := signal.NotifyContext(ctx, syscall.SIGINT, syscall.SIGTERM)
		defer stop()

		d, err := sql.Open("sqlite", dbFile)
		if err != nil {
			log.Err(err).Msg("db failed")
			return err
		}
		db.InitialMigration(d)
		q := db.New(d)

		log.Info().Msg("downloading initial data...")
		stations, err := sncf.Download(ctx)
		if err != nil {
			return fmt.Errorf("failed to download initial data: %w", err)
		}
		log.Info().Msg("clearing database...")
		if err := q.ClearWithTx(ctx, d); err != nil {
			return fmt.Errorf("failed to clear db: %w", err)
		}
		log.Info().Msg("inserting new data in database...")
		if err := q.CreateManyStationsWithTx(ctx, d, mappers.StationsFromSNCF(stations)...); err != nil {
			return fmt.Errorf("failed to insert initial data: %w", err)
		}
		log.Info().Msg("initialized database successfully")

		tlsConfig, err := internaltls.SetupServerTLSConfig(
			certFile,
			certFile,
			clientCAFile,
		)
		if err != nil {
			return fmt.Errorf("cannot setup server TLS config: %w", err)
		}

		jwks := jwks.NewService(jwksURL)
		if err := jwks.Refresh(ctx); err != nil {
			return fmt.Errorf("failed to refresh JWKS: %w", err)
		}

		r := chi.NewMux()
		r.Use(middleware.RequestID)
		r.Use(middleware.RealIP)
		r.HandleFunc("GET /debug/pprof/", pprof.Index)
		r.HandleFunc("GET /debug/pprof/cmdline", pprof.Cmdline)
		r.HandleFunc("GET /debug/pprof/profile", pprof.Profile)
		r.HandleFunc("GET /debug/pprof/symbol", pprof.Symbol)
		r.HandleFunc("GET /debug/pprof/trace", pprof.Trace)
		r.HandleFunc("GET /debug/pprof/delta_heap", godeltaprof.Heap)
		r.HandleFunc("GET /debug/pprof/delta_block", godeltaprof.Block)
		r.HandleFunc("GET /debug/pprof/delta_mutex", godeltaprof.Mutex)
		r.Handle(healthv1connect.NewHealthHandler(health.New()))

		path, handler := trainstationv1alpha1connect.NewStationAPIHandler(station.NewAPIHandler(q))
		r.Handle(path, jwks.AuthMiddleware(handler))

		ongoingCtx, stopOngoingGracefully := context.WithCancel(
			log.Logger.WithContext(context.Background()),
		)
		srv := &http.Server{
			Addr:         listenAddress,
			TLSConfig:    tlsConfig,
			Handler:      r,
			ReadTimeout:  5 * time.Second,
			WriteTimeout: 10 * time.Second,
			IdleTimeout:  30 * time.Second,
			BaseContext:  func(_ net.Listener) context.Context { return ongoingCtx },
		}

		go jwks.RefreshLoop(ctx, jwksRefreshPeriod)

		go func() {
			log.Info().Str("address", srv.Addr).Msg("starting private server")
			if err = srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
				panic(err)
			}
		}()

		// ---SHUTDOWN---
		<-ctx.Done()
		stop()
		log.Info().Msg("Received shutdown signal, shutting down.")

		// Give time for readiness check to propagate
		time.Sleep(_readinessDrainDelay)
		log.Info().Msg(
			"Readiness check propagated, now waiting for ongoing requests to finish.",
		)

		shutdownCtx, cancel := context.WithTimeout(context.Background(), _shutdownPeriod)
		defer cancel()
		err = srv.Shutdown(shutdownCtx)
		if err != nil {
			log.Info().Msg(
				"Failed to wait for ongoing requests to finish, waiting for forced cancellation.",
			)
			stopOngoingGracefully()
			time.Sleep(_shutdownHardPeriod)
		}
		stopOngoingGracefully()
		log.Info().Msg("Server shut down gracefully.")

		return nil
	},
}

func init() {
	logLevel := os.Getenv("LOG_LEVEL")
	var l zerolog.Level
	if err := l.UnmarshalText([]byte(logLevel)); err != nil || l == zerolog.NoLevel {
		l = zerolog.DebugLevel
	}
	zerolog.SetGlobalLevel(l)
	zerolog.ErrorStackMarshaler = pkgerrors.MarshalStack
	log.Logger = log.Output(os.Stderr).With().
		Caller().
		Logger()
	log.WithLevel(l).Str("set_level", zerolog.LevelFieldMarshalFunc(l)).Msg("log level")
}

func main() {
	ctx := context.Background()
	_ = godotenv.Load(".env.local")
	_ = godotenv.Load(".env")
	if err := app.Run(ctx, os.Args); err != nil {
		log.Fatal().Err(err).Msg("app crashed")
	}
}
