import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm/dist/typeorm.module';
import { BookController } from 'controllers/book.controller';
import { Book } from 'models/book';
import { BookService } from 'services/book.service';

@Module({
  imports: [TypeOrmModule.forFeature([Book])],
  controllers: [BookController],
  providers: [BookService],
})
export class BookModule {}
