import { ApiProperty } from '@nestjs/swagger/dist';
import { IsString } from 'class-validator';

export class SearchBookDto {
  @IsString()
  @ApiProperty({
    description: 'Search term for the book',
  })
  term: string;
}
