import { HttpModule } from '@nestjs/axios';
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm/dist/typeorm.module';
import { TrainStationController } from 'controllers/train-station.controller';
import { Station } from 'entities/station';
import { TrainStationService } from 'services/train-station.service';

@Module({
  imports: [TypeOrmModule.forFeature([Station]), HttpModule],
  controllers: [TrainStationController],
  providers: [TrainStationService],
  exports: [TrainStationService],
})
export class TrainStationModule {}
