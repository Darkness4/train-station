import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { CreateBookDto } from 'controllers/dtos/create-book.dto';
import { Book } from 'models/book';
import { LessThanOrEqual, Like, Repository } from 'typeorm';

@Injectable()
export class BookService {
  constructor(
    @InjectRepository(Book)
    private booksRepository: Repository<Book>,
  ) {}

  async addBook(createBookDto: CreateBookDto): Promise<Book> {
    let Book: Book = createBookDto;
    return this.booksRepository.create(Book);
  }

  getBook(title: string): Promise<Book> {
    return this.booksRepository.findOne(title);
  }

  getBooksOf(author: string, start: number, limit: number): Promise<Book[]> {
    return this.booksRepository.find({
      where: { author: author },
      skip: start,
      take: limit,
    });
  }

  getAllBooks(start: number, limit: number): Promise<Book[]> {
    return this.booksRepository.find({
      skip: start,
      take: limit,
    });
  }

  getTotalNumberOfBooks(): Promise<number> {
    return this.booksRepository.count();
  }

  getBooksPublishedBefore(date: Date): Promise<Book[]> {
    return this.booksRepository.find({ date: LessThanOrEqual(date) });
  }

  async delete(title: string): Promise<number> {
    let result = await this.booksRepository.delete(title);
    return result.affected;
  }

  clear(): Promise<void> {
    return this.booksRepository.clear();
  }

  async findByKeywords(term: string): Promise<Book[]> {
    return this.booksRepository.find({
      where: [{ title: Like(`%${term}%`) }, { author: Like(`%${term}%`) }],
    });
  }
}
