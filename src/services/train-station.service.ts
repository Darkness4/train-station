import { HttpService, Injectable, Logger } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { TypeOrmCrudService } from '@nestjsx/crud-typeorm/lib/typeorm-crud.service';
import { Station } from 'models/station';
import { Repository } from 'typeorm';
import { mergeMap } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Injectable()
export class TrainStationService extends TypeOrmCrudService<Station> {
  subscription?: Subscription;

  constructor(
    client: HttpService,
    @InjectRepository(Station) repo: Repository<Station>,
  ) {
    super(repo);

    // Initially downloading the data.
    this.subscription = client
      .get<Station[]>(
        'https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json',
      )
      .pipe(
        mergeMap((response) =>
          Promise.all(response.data.map((e) => repo.save(e))),
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
}
