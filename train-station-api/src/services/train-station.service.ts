import { HttpService, Injectable, Logger } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { TypeOrmCrudService } from '@nestjsx/crud-typeorm/lib/typeorm-crud.service';
import { Station } from 'models/station';
import { Repository } from 'typeorm';

@Injectable()
export class TrainStationService extends TypeOrmCrudService<Station> {
  constructor(
    client: HttpService,
    @InjectRepository(Station) repo: Repository<Station>,
  ) {
    super(repo);

    client
      .get<Station[]>(
        'https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json',
      )
      .toPromise()
      .then((response) => {
        if (response.status == 200) {
          return Promise.all(response.data.map((e) => repo.save(e)));
        } else {
          Logger.error(
            "Couldn't download the initial data.",
            'TrainStationService',
          );
        }
      })
      .then((array) =>
        Logger.log(
          `Database initialized. Total: ${array.length} rows.`,
          'TrainStationService',
        ),
      )
      .catch((reason) =>
        Logger.error(
          `Couldn't download the initial data. ${reason}`,
          'TrainStationService',
        ),
      );
  }
}
