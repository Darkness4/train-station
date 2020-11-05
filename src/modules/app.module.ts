import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm/dist';
import { AppController } from 'controllers/app.controller';
import { Fields } from 'models/fields';
import { Geometry } from 'models/geometry';
import { Station } from 'models/station';
import { AppService } from 'services/app.service';
import { TrainStationModule } from './train-station.module';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'sqlite',
      database: 'cache.sqlite3',
      entities: [Station, Fields, Geometry],
      synchronize: true,
    }),
    TrainStationModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
