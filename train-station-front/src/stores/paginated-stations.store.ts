import StationRepository from '$lib/api/train-station';
import type { Paginated } from '$lib/entities/paginated';
import type { Station } from '$lib/entities/station';
import { writable } from 'svelte/store';

export const initialState: Paginated<Station> = {
	count: 0,
	data: [],
	page: 1,
	pageCount: 1,
	total: 0
};
function createPaginatedStationsStore() {
	const { subscribe, update, set } = writable<Paginated<Station>>({ ...initialState });

	return {
		subscribe,
		refresh: async () => {
			const result = await StationRepository.find();
			set(result);
		},
		load: async (page: number) => {
			const result = await StationRepository.find({ page: page });
			set(result);
		}
	};
}
export const paginatedStationsStore = createPaginatedStationsStore();
