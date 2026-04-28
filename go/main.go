package main

import (
	"database/sql"
	"encoding/base64"
	"net"
	"os"

	"github.com/Darkness4/train-station/go/api/auth"
	"github.com/Darkness4/train-station/go/api/health"
	"github.com/Darkness4/train-station/go/api/station"
	"github.com/Darkness4/train-station/go/db"
	"github.com/Darkness4/train-station/go/db/mappers"
	authv1alpha1 "github.com/Darkness4/train-station/go/gen/go/auth/v1alpha1"
	healthv1 "github.com/Darkness4/train-station/go/gen/go/grpc/health/v1"
	trainstationv1alpha1 "github.com/Darkness4/train-station/go/gen/go/trainstation/v1alpha1"
	"github.com/Darkness4/train-station/go/jwt"
	"github.com/Darkness4/train-station/go/logger"
	"github.com/Darkness4/train-station/go/sncf"
	"github.com/grpc-ecosystem/go-grpc-middleware/v2/interceptors/logging"
	"github.com/joho/godotenv"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	"github.com/urfave/cli/v2"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"

	_ "modernc.org/sqlite"
)

var (
	listenAddress string

	tls      bool
	keyFile  string
	certFile string

	dbFile string

	jwtSecret []byte

	version string
)

func init() {
	log.Logger = log.Logger.Level(zerolog.InfoLevel)
	zerolog.SetGlobalLevel(zerolog.InfoLevel)
}

var flags = []cli.Flag{
	&cli.StringFlag{
		Name:        "grpc.listen-address",
		Value:       ":3000",
		Usage:       "Address to listen on. Is used for receiving job status via the job completion plugin.",
		Destination: &listenAddress,
		EnvVars:     []string{"LISTEN_ADDRESS"},
	},
	&cli.BoolFlag{
		Name:        "tls",
		Value:       false,
		Destination: &tls,
		Usage:       "Enable TLS for GRPC.",
		EnvVars:     []string{"TLS_ENABLE"},
	},
	&cli.StringFlag{
		Name:        "tls.key-file",
		Value:       "",
		Destination: &keyFile,
		Usage:       "TLS Private Key file.",
		EnvVars:     []string{"TLS_KEY"},
	},
	&cli.StringFlag{
		Name:        "tls.cert-file",
		Value:       "",
		Destination: &certFile,
		Usage:       "TLS Certificate file.",
		EnvVars:     []string{"TLS_CERT"},
	},
	&cli.StringFlag{
		Name:        "db.path",
		Value:       "./db.sqlite3",
		Destination: &dbFile,
		Usage:       "SQLite3 database file path.",
		EnvVars:     []string{"DB_PATH"},
	},
	&cli.StringFlag{
		Name:     "jwt.secret",
		Required: true,
		Usage:    "JWT secret key.",
		EnvVars:  []string{"JWT_SECRET"},
		Action: func(ctx *cli.Context, s string) (err error) {
			jwtSecret, err = base64.StdEncoding.DecodeString(s)
			if err != nil {
				log.Panic().Err(err).Msg("failed to decode JWT")
			}
			return nil
		},
	},
	&cli.BoolFlag{
		Name:    "debug",
		EnvVars: []string{"DEBUG"},
		Value:   false,
		Action: func(ctx *cli.Context, s bool) error {
			if s {
				log.Logger = log.Logger.Level(zerolog.DebugLevel)
				zerolog.SetGlobalLevel(zerolog.DebugLevel)
			}
			return nil
		},
	},
}

var app = &cli.App{
	Name:    "train-station-api",
	Usage:   "The train station API",
	Flags:   flags,
	Version: version,
	Suggest: true,
	Action: func(cCtx *cli.Context) error {
		ctx := cCtx.Context
		j := jwt.New(jwtSecret)

		d, err := sql.Open("sqlite", dbFile)
		if err != nil {
			log.Err(err).Msg("db failed")
			return err
		}
		db.InitialMigration(d)
		q := db.New(d)

		go func() {
			log.Info().Msg("downloading initial data...")
			stations, err := sncf.Download(ctx)
			if err != nil {
				log.Panic().Err(err).Msg("failed to download initial data")
			}
			log.Info().Msg("clearing database...")
			if err := q.ClearWithTx(ctx, d); err != nil {
				log.Panic().Err(err).Msg("failed to clear db")
			}
			log.Info().Msg("inserting new data in database...")
			if err := q.CreateManyStationsWithTx(ctx, d, mappers.StationsFromSNCF(stations)...); err != nil {
				log.Panic().Err(err).Msg("failed to insert initial data")
			}
			log.Info().Msg("initialized database successfully")
		}()

		lis, err := net.Listen("tcp", listenAddress)
		if err != nil {
			log.Err(err).Msg("listen failed")
			return err
		}

		opts := []grpc.ServerOption{
			grpc.ChainUnaryInterceptor(
				logging.UnaryServerInterceptor(
					logger.InterceptorLogger(log.Logger),
				),
			),
			grpc.ChainStreamInterceptor(
				logging.StreamServerInterceptor(
					logger.InterceptorLogger(log.Logger),
				),
			),
		}
		if tls {
			creds, err := credentials.NewServerTLSFromFile(certFile, keyFile)
			if err != nil {
				log.Fatal().Err(err).Msg("failed to load certificates")
			}
			opts = append(opts, grpc.Creds(creds))
		}
		server := grpc.NewServer(opts...)
		healthv1.RegisterHealthServer(
			server,
			health.New(),
		)
		trainstationv1alpha1.RegisterStationAPIServer(
			server,
			station.New(q, j),
		)
		authv1alpha1.RegisterAuthAPIServer(
			server,
			auth.NewAPI(j),
		)

		log.Info().Msg("serving...")

		return server.Serve(lis)
	},
}

func main() {
	_ = godotenv.Load(".env.local")
	_ = godotenv.Load(".env")
	if err := app.Run(os.Args); err != nil {
		log.Fatal().Err(err).Msg("app crashed")
	}
}
