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
		load: async (token: string, params?: { s?: string | null; page?: number | null }) => {
			const result = await StationRepository.find(token, params);
			set(result);
		},
		makeFavorite: async (id: string, value: boolean, token: string) => {
			const result = await StationRepository.makeFavoriteById(id, { is_favorite: value }, token);
			update((state) => {
				const index = state.data.findIndex((value) => value.recordid === result.recordid);
				state.data[index] = result;
				return state;
			});
		}
	};
}
export const paginatedStationsStore = createPaginatedStationsStore();
