import StationRepository from '$lib/api/train-station';
import type { Station } from '$lib/entities/station';
import { writable } from 'svelte/store';

export const initialState: Station | null = null;
function createStationStore() {
	const { subscribe, update, set } = writable<Station | null>(initialState);

	return {
		subscribe,
		load: async (id: string) => {
			const result = await StationRepository.findById(id);
			set(result);
		}
	};
}
export const stationStore = createStationStore();
