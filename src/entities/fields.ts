import { ApiProperty } from '@nestjs/swagger';
import {
  Column,
  Entity,
  JoinColumn,
  OneToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Geometry } from './geometry';

@Entity('fields')
export class Fields {
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  commune?: string;

  @ApiProperty()
  @Column({ nullable: true })
  y_wgs84?: number;

  @ApiProperty()
  @Column({ nullable: true })
  x_wgs84?: number;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  libelle?: string;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  idgaia?: string;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  voyageurs?: string;

  @ApiProperty()
  @Column('simple-array', { nullable: true })
  geo_point_2d?: number[];

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  code_ligne?: string;

  @ApiProperty()
  @Column({ nullable: true })
  x_l93?: number;

  @ApiProperty()
  @Column('simple-array', { nullable: true })
  c_geo?: number[];

  @ApiProperty()
  @Column({ nullable: true })
  rg_troncon?: number;

  @ApiProperty()
  @OneToOne(() => Geometry, {
    cascade: true,
    nullable: true,
  })
  @JoinColumn()
  geo_shape?: Geometry;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  pk?: string;

  @ApiProperty()
  @Column({ nullable: true })
  idreseau?: number;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  departemen?: string;

  @ApiProperty()
  @Column({ nullable: true })
  y_l93?: number;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  fret?: string;
}
