import { NestFactory } from '@nestjs/core';
import { FastifyAdapter } from '@nestjs/platform-fastify/adapters/fastify-adapter';
import { NestFastifyApplication } from '@nestjs/platform-fastify/interfaces/nest-fastify-application.interface';
import { DocumentBuilder } from '@nestjs/swagger/dist/document-builder';
import { SwaggerModule } from '@nestjs/swagger/dist/swagger-module';
import { AppModule } from 'modules/app.module';
import compression from 'fastify-compress';

async function bootstrap() {
  const port = parseInt(<string>process.env.PORT, 10) || 8080;
  const app = await NestFactory.create<NestFastifyApplication>(
    AppModule,
    new FastifyAdapter(),
  );

  // Setup Swagger
  const options = new DocumentBuilder()
    .setTitle('SNCF Train Station Alternative API')
    .setDescription(
      'An relay API for the SNCF that only refreshes on reboot...',
    )
    .setVersion('1.0')
    .addTag('train')
    .build();
  const document = SwaggerModule.createDocument(app, options);
  SwaggerModule.setup('api', app, document);

  // Compress Data
  app.register(compression);

  await app.listen(port, '0.0.0.0');
}
bootstrap();
