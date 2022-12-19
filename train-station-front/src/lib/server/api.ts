import { ChannelCredentials } from '@grpc/grpc-js';
import { GrpcTransport } from '@protobuf-ts/grpc-transport';
import { StationAPIClient } from '$gen/ts/trainstation/v1alpha1/station.client';

const TRAIN_API_BASE_URL = 'localhost:3000';

const transport = new GrpcTransport({
	host: TRAIN_API_BASE_URL,
	channelCredentials: ChannelCredentials.createInsecure()
});

const client = new StationAPIClient(transport);

export default client;
