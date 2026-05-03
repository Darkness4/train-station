import { type Client, createClient } from '@connectrpc/connect';
import { createConnectTransport } from '@connectrpc/connect-node';
import { env } from '$env/dynamic/private';
import { StationAPI } from '$gen/ts/trainstation/v1alpha1/station_pb';

let instance: Client<typeof StationAPI> | null = null;

export const getStationClient = () => {
	if (instance) return instance;

	if (!env.TRAIN_API_BASE_URL) {
		throw new Error('Missing TRAIN_API_BASE_URL');
	}

	instance = createClient(
		StationAPI,
		createConnectTransport({
			baseUrl: env.TRAIN_API_BASE_URL,
			httpVersion: '1.1'
		})
	);

	return instance;
};
