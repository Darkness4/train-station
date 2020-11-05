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
  @Column('varchar', { length: 255 })
  commune: string;

  @ApiProperty()
  @Column()
  y_wgs84: number;

  @ApiProperty()
  @Column()
  x_wgs84: number;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  libelle: string;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  idgaia: string;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  voyageurs: string;

  @ApiProperty()
  @Column('simple-array')
  geo_point_2d: number[];

  @ApiProperty()
  @Column('varchar', { length: 255 })
  code_ligne: string;

  @ApiProperty()
  @Column()
  x_l93: number;

  @ApiProperty()
  @Column('simple-array')
  c_geo: number[];

  @ApiProperty()
  @Column()
  rg_troncon: number;

  @ApiProperty()
  @OneToOne(() => Geometry, {
    eager: true,
    cascade: true,
  })
  @JoinColumn()
  geo_shape: Geometry;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  pk: string;

  @ApiProperty()
  @Column()
  idreseau: number;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  departemen: string;

  @ApiProperty()
  @Column()
  y_l93: number;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  fret: string;
}
