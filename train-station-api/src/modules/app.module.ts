import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm/dist';
import { AppController } from 'controllers/app.controller';
import { Book } from 'models/book';
import { AppService } from 'services/app.service';
import { BookModule } from './book.module';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'sqlite',
      database: ':memory:',
      entities: [Book],
      synchronize: true,
    }),
    BookModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
