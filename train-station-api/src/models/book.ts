import { ApiProperty } from '@nestjs/swagger';
import { Column, Entity, PrimaryColumn } from 'typeorm';

@Entity('book')
export class Book {
  @PrimaryColumn()
  @ApiProperty()
  title: string;

  @Column()
  @ApiProperty()
  author: string;

  @Column({ type: 'datetime' })
  @ApiProperty()
  date: Date;
}
