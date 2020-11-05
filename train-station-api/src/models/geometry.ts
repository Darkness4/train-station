import { ApiProperty } from '@nestjs/swagger';
import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('geometry')
export class Geometry {
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty()
  @Column('varchar', { length: 255 })
  type: string;

  @ApiProperty()
  @Column('simple-array')
  coordinates: number[];
}
