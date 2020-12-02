import { ApiProperty } from '@nestjs/swagger';
import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('geometry')
export class Geometry {
  @PrimaryGeneratedColumn()
  id: number;

  @ApiProperty()
  @Column('varchar', { length: 255, nullable: true })
  type?: string;

  @ApiProperty({ description: 'Geographical coordinates' })
  @Column('simple-array', { nullable: true })
  coordinates?: number[];
}
