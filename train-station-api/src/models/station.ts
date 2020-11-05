import { ApiProperty } from '@nestjs/swagger';
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

  @ApiProperty()
  @Column({ default: false })
  is_favorite: boolean;

  @ApiProperty()
  @OneToOne(() => Fields, {
    eager: true,
    cascade: true,
    nullable: true,
  })
  @JoinColumn()
  fields?: Fields;

  @ApiProperty()
  @OneToOne(() => Geometry, {
    eager: true,
    cascade: true,
    nullable: true,
  })
  @JoinColumn()
  geometry?: Geometry;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  record_timestamp?: string;
}
