import { Controller } from '@nestjs/common';
import { Crud, CrudController } from '@nestjsx/crud';
import { Station } from 'models/station';
import { TrainStationService } from 'services/train-station.service';

@Crud({
  model: {
    type: Station,
  },
  routes: {
    only: ['getOneBase', 'replaceOneBase', 'createOneBase', 'getManyBase'],
  },
  params: {
    id: {
      type: 'string',
      primary: true,
      field: 'recordid',
    },
  },
  query: {
    limit: 10,
    alwaysPaginate: true,
    join: {
      fields: {
        eager: true,
      },
      geometry: {
        eager: true,
      },
    },
  },
})
@Controller('stations')
export class TrainStationController implements CrudController<Station> {
  constructor(readonly service: TrainStationService) {}
}
