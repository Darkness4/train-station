package root

import (
	"github.com/Darkness4/train-station-api/internal"
	"github.com/Darkness4/train-station-api/pkg/core/root/data"
	"github.com/Darkness4/train-station-api/pkg/core/root/domain"
	"github.com/Darkness4/train-station-api/pkg/core/root/presentation"
	"github.com/sirupsen/logrus"
	"github.com/spf13/viper"
)

type Component struct {
	Data         *data.Module
	Domain       *domain.Module
	Presentation *presentation.Module
}

func ComponentInit() *Component {
	level, err := logrus.ParseLevel(viper.GetString("LOG_LEVEL"))
	if err != nil {
		internal.Logger.SetLevel(logrus.DebugLevel)
		internal.Logger.Warnf("Couldn't parse LOG_LEVEL %s\n", viper.GetString("LOG_LEVEL"))
	} else {
		internal.Logger.SetLevel(level)
	}

	data := data.NewModule()
	domain := domain.NewModule(data)
	presentation := presentation.NewModule(data, domain)

	return &Component{
		Data:         data,
		Domain:       domain,
		Presentation: presentation,
	}
}

func (sl *Component) Dispose() {
	sl.Presentation.Dispose()
	sl.Domain.Dispose()
	sl.Data.Dispose()
}
