import { NestFactory } from '@nestjs/core';
import { FastifyAdapter } from '@nestjs/platform-fastify/adapters/fastify-adapter';
import { NestFastifyApplication } from '@nestjs/platform-fastify/interfaces/nest-fastify-application.interface';
import { DocumentBuilder } from '@nestjs/swagger/dist/document-builder';
import { SwaggerModule } from '@nestjs/swagger/dist/swagger-module';
import { AppModule } from 'modules/app.module';

async function bootstrap() {
  const port = parseInt(<string>process.env.PORT, 10) || 3000;
  const app = await NestFactory.create<NestFastifyApplication>(
    AppModule,
    new FastifyAdapter(),
  );

  const options = new DocumentBuilder()
    .setTitle('Bookx example')
    .setDescription('The books API description')
    .setVersion('1.0')
    .addTag('books')
    .build();

  const document = SwaggerModule.createDocument(app, options);
  SwaggerModule.setup('api', app, document);

  await app.listen(port);
}
bootstrap();
