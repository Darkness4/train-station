import { Controller, Get, Res } from '@nestjs/common';

@Controller('/')
export class AppController {
  @Get()
  index(@Res() res) {
    return res.status(302).redirect('/api');
  }
}
