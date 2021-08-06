import axios from 'axios';

import type { Paginated } from '$lib/entities/paginated';
import type { Station } from '$lib/entities/station';

const DEFAULT_LIMIT = 20;
const DEFAULT_PAGE = 1;

const trainStationApi = axios.create({
	baseURL: 'https://train.the-end-is-never-the-end.pw/api'
});

const StationRepository = {
	async find(
		token: string,
		options?: {
			s?: string | null;
			limit?: number | null;
			page?: number | null;
		}
	): Promise<Paginated<Station>> {
		const apiQuery = JSON.stringify({
			libelle: {
				$cont: options?.s ?? ''
			}
		});

		const r = await trainStationApi.get<Paginated<Station>>('/stations/', {
			params: {
				s: apiQuery,
				limit: options?.limit ?? DEFAULT_LIMIT,
				page: options?.page ?? DEFAULT_PAGE
			},
			headers: { Authorization: `Bearer ${token}` }
		});

		return r.data;
	},

	async create(body: Station, token: string): Promise<Station> {
		const r = await trainStationApi.post<Station>('/stations/', body, {
			headers: { Authorization: `Bearer ${token}` }
		});

		return r.data;
	},

	async findById(id: string, token: string): Promise<Station> {
		const r = await trainStationApi.get<Station>(`/stations/${id}`, {
			headers: { Authorization: `Bearer ${token}` }
		});

		return r.data;
	},

	async makeFavoriteById(
		id: string,
		body: { is_favorite: boolean },
		token: string
	): Promise<Station> {
		const r = await trainStationApi.post<Station>(`/stations/${id}/makeFavorite`, body, {
			headers: { Authorization: `Bearer ${token}` }
		});

		return r.data;
	}
};
export default StationRepository;
