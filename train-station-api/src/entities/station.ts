import { ApiProperty } from '@nestjs/swagger';
import { StationModel } from 'models/station.model';
import {
  BaseEntity,
  Column,
  Entity,
  JoinColumn,
  OneToOne,
  PrimaryColumn,
} from 'typeorm';
import { Fields } from './fields';
import { Geometry } from './geometry';

@Entity('station')
export class Station extends BaseEntity {
  @ApiProperty()
  @PrimaryColumn('varchar', { length: 255 })
  recordid: string;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  datasetid: string;

  @ApiProperty({
    description: 'This station is one of the favorite of our lovely user.',
  })
  @Column({ default: false })
  is_favorite: boolean;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  libelle: string;

  @ApiProperty()
  @OneToOne(() => Fields, {
    cascade: true,
    nullable: true,
  })
  @JoinColumn()
  fields?: Fields;

  @ApiProperty()
  @OneToOne(() => Geometry, {
    cascade: true,
    nullable: true,
  })
  @JoinColumn()
  geometry?: Geometry;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  record_timestamp?: string;

  static fromModel(model: StationModel): Station {
    let entity = new Station();
    entity.recordid = model.recordid;
    entity.datasetid = model.datasetid;
    entity.libelle = model.fields.libelle;
    entity.fields = model.fields;
    entity.geometry = model.geometry;
    entity.record_timestamp = model.record_timestamp;
    return entity;
  }
}
