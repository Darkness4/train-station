package main

import (
	"database/sql"
	"net"
	"os"

	"github.com/Darkness4/train-station-api/api/health"
	"github.com/Darkness4/train-station-api/api/station"
	"github.com/Darkness4/train-station-api/db"
	"github.com/Darkness4/train-station-api/db/mappers"
	healthv1 "github.com/Darkness4/train-station-api/gen/go/grpc/health/v1"
	trainstationv1alpha1 "github.com/Darkness4/train-station-api/gen/go/trainstation/v1alpha1"
	"github.com/Darkness4/train-station-api/jwt"
	"github.com/Darkness4/train-station-api/logger"
	"github.com/Darkness4/train-station-api/sncf"
	"github.com/joho/godotenv"
	"github.com/urfave/cli/v2"
	"github.com/volatiletech/sqlboiler/v4/boil"
	"go.uber.org/zap"
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

	jwtSecret string
)

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
		Name:        "jwt.secret",
		Required:    true,
		Destination: &jwtSecret,
		Usage:       "JWT secret key.",
		EnvVars:     []string{"JWT Secret"},
	},
	&cli.BoolFlag{
		Name:    "debug",
		EnvVars: []string{"DEBUG"},
		Value:   false,
		Action: func(ctx *cli.Context, s bool) error {
			if s {
				logger.EnableDebug()
				boil.DebugMode = true
			}
			return nil
		},
	},
}

var app = &cli.App{
	Name:    "train-station-api",
	Usage:   "The train station API",
	Flags:   flags,
	Suggest: true,
	Action: func(cCtx *cli.Context) error {
		ctx := cCtx.Context
		j := jwt.NewValidator(jwtSecret)

		d, err := sql.Open("sqlite", dbFile)
		if err != nil {
			logger.I.Error("db failed", zap.Error(err))
			return err
		}

		go func() {
			logger.I.Info("downloading initial data...")
			stations, err := sncf.Download(ctx)
			if err != nil {
				logger.I.Panic("failed to download initial data", zap.Error(err))
			}
			logger.I.Info("clearing database...")
			if err := db.Clear(ctx, d); err != nil {
				logger.I.Panic("failed to clear db", zap.Error(err))
			}
			logger.I.Info("inserting new data in database...")
			if err := db.CreateManyStation(ctx, d, mappers.StationsFromSNCF(stations)); err != nil {
				logger.I.Panic("failed to insert initial data", zap.Error(err))
			}
			logger.I.Info("initialized database successfully")
		}()

		lis, err := net.Listen("tcp", listenAddress)
		if err != nil {
			logger.I.Error("listen failed", zap.Error(err))
			return err
		}

		opts := []grpc.ServerOption{}
		if tls {
			creds, err := credentials.NewServerTLSFromFile(certFile, keyFile)
			if err != nil {
				logger.I.Fatal("failed to load certificates", zap.Error(err))
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
			station.New(d, j),
		)

		logger.I.Info("serving...")

		return server.Serve(lis)
	},
}

func main() {
	_ = godotenv.Load(".env.local")
	_ = godotenv.Load(".env")
	if err := app.Run(os.Args); err != nil {
		logger.I.Fatal("app crashed", zap.Error(err))
	}
}
