import { Controller } from '@nestjs/common';
import { Override } from '@nestjsx/crud';
import {
  Crud,
  CrudController,
  CrudRequest,
  ParsedRequest,
} from '@nestjsx/crud';
import { Station } from 'entities/station';
import { TrainStationService } from 'services/train-station.service';

@Crud({
  model: {
    type: Station,
  },
  routes: {
    only: ['getOneBase', 'updateOneBase', 'createOneBase', 'getManyBase'],
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
    sort: [
      {
        field: 'libelle',
        order: 'ASC',
      },
    ],
  },
})
@Controller('stations')
export class TrainStationController implements CrudController<Station> {
  constructor(readonly service: TrainStationService) {}

  @Override()
  getOne(@ParsedRequest() req: CrudRequest): Promise<Station> {
    return this.service.getOneDetail(req);
  }
}
