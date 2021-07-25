import { writable } from 'svelte/store';

import StationRepository from '$lib/api/train-station';
import type { Station } from '$lib/entities/station';

export const initialState: Station | null = null;
function createStationStore() {
	const { subscribe, set } = writable<Station | null>(initialState);

	return {
		subscribe,
		load: async (id: string, token: string) => {
			const result = await StationRepository.findById(id, token);
			set(result);
		}
	};
}
export const stationStore = createStationStore();
