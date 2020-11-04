import { ApiProperty } from '@nestjs/swagger';

export class Links {
  @ApiProperty()
  next: string;

  @ApiProperty()
  prev: string;

  @ApiProperty()
  self: string;
}

export class PaginatedDto<T = any> {
  @ApiProperty()
  _links: Links;

  @ApiProperty()
  limit: number;

  @ApiProperty()
  results: T[];

  @ApiProperty()
  size: number;

  @ApiProperty()
  start: number;
}
