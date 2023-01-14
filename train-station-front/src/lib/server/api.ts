import { ChannelCredentials } from '@grpc/grpc-js';
import { GrpcTransport } from '@protobuf-ts/grpc-transport';
import { StationAPIClient } from '$gen/ts/trainstation/v1alpha1/station.client';
import { AuthAPIClient } from '$gen/ts/auth/v1alpha1/auth.client';
import { env } from '$env/dynamic/private';

const transport = new GrpcTransport({
	host: env.TRAIN_API_BASE_URL ?? 'localhost:3000',
	channelCredentials: ChannelCredentials.createInsecure()
});

export const stationClient = new StationAPIClient(transport);

export const authClient = new AuthAPIClient(transport);
