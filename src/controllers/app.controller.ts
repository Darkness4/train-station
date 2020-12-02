import { Controller, Get, Res } from '@nestjs/common';
import { FastifyReply } from 'fastify';

@Controller('/')
export class AppController {
  /**
   * The '/' route, which redirect to our API Client
   * @param res Response object bulder
   */
  @Get()
  index(@Res() res: FastifyReply) {
    return res.status(302).redirect('/api');
  }
}
