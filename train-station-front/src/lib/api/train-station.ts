import type { Paginated } from '$lib/entities/paginated';
import type { Station } from '$lib/entities/station';
import axios from 'axios';

const DEFAULT_LIMIT = 20;
const DEFAULT_PAGE = 1;

const trainStationApi = axios.create({
	baseURL: 'https://train.the-end-is-never-the-end.pw/api'
});

const StationRepository = {
	async find(options?: {
		s?: string | null;
		limit?: number | null;
		page?: number | null;
	}): Promise<Paginated<Station>> {
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
			}
		});

		return r.data;
	},

	async create(body: Station): Promise<Station> {
		const r = await trainStationApi.post<Station>('/stations/', body);

		return r.data;
	},

	async findById(id: string): Promise<Station> {
		const r = await trainStationApi.get<Station>(`/stations/${id}`);

		return r.data;
	},

	async updateById(id: string, body: Station): Promise<Station> {
		const r = await trainStationApi.patch<Station>(`/stations/${id}`, body);

		return r.data;
	}
};
export default StationRepository;
