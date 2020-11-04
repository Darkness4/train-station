import { ApiProperty } from '@nestjs/swagger/dist/decorators';
import { IsDateString, IsNotEmpty, IsString } from 'class-validator';
import { Book } from 'models/book';

export class CreateBookDto extends Book {
  @IsNotEmpty()
  @IsString()
  @ApiProperty({
    description: 'Title of the book',
  })
  title: string;

  @IsNotEmpty()
  @IsString()
  @ApiProperty({
    description: 'Author of the book',
  })
  author: string;

  @IsNotEmpty()
  @IsDateString()
  @ApiProperty({
    description: 'Published date of the book',
  })
  date: Date;
}
