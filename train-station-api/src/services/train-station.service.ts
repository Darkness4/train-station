import {
  HttpService,
  Injectable,
  Logger,
  OnModuleDestroy,
  OnModuleInit,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { TypeOrmCrudService } from '@nestjsx/crud-typeorm/lib/typeorm-crud.service';
import { Station } from 'entities/station';
import { Repository } from 'typeorm';
import { mergeMap } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { StationModel } from 'models/station.model';
import { CrudRequest, GetManyDefaultResponse } from '@nestjsx/crud';

@Injectable()
export class TrainStationService
  extends TypeOrmCrudService<Station>
  implements OnModuleInit, OnModuleDestroy {
  subscription?: Subscription = null;

  constructor(
    private client: HttpService,
    @InjectRepository(Station) repo: Repository<Station>,
  ) {
    super(repo);
  }

  onModuleInit() {
    // Initially downloading the data.
    this.subscription = this.client
      .get<StationModel[]>(
        'https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json',
      )
      .pipe(
        mergeMap((response) =>
          Promise.all(response.data.map((e) => Station.fromModel(e).save())),
        ),
      )
      .subscribe(
        (array) =>
          Logger.log(
            `Database initialized. Total: ${array.length} rows.`,
            'TrainStationService',
          ),
        (reason) =>
          Logger.error(
            `Couldn't download the initial data. ${reason}`,
            'TrainStationService',
          ),
      );
  }

  async getManySummary(
    req: CrudRequest,
  ): Promise<GetManyDefaultResponse<Station> | Station[]> {
    const { parsed, options } = req;
    options.query.join = {};
    const builder = await this.createBuilder(parsed, options);
    return this.doGetMany(builder, parsed, options);
  }

  getOneDetail(req: CrudRequest): Promise<Station> {
    req.options.query.join = {
      fields: {
        eager: true,
      },
      geometry: {
        eager: true,
      },
      'fields.geo_shape': {
        eager: true,
      },
    };
    return this.getOne(req);
  }

  onModuleDestroy() {
    this.subscription?.unsubscribe();
  }
}
