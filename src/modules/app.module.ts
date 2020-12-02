import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm/dist';
import { AppController } from 'controllers/app.controller';
import { Fields } from 'entities/fields';
import { Geometry } from 'entities/geometry';
import { Station } from 'entities/station';
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
})
export class AppModule {}
