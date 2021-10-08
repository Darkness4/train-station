import type { Paginated } from '$lib/entities/paginated';
import type { Station } from '$lib/entities/station';

const DEFAULT_LIMIT = 20;
const DEFAULT_PAGE = 1;
export const TRAIN_API_BASE_URL = 'https://train.the-end-is-never-the-end.pw/api';

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

		const url = new URL(TRAIN_API_BASE_URL + '/stations/');
		const params = new URLSearchParams({
			s: apiQuery,
			limit: (options?.limit ?? DEFAULT_LIMIT).toString(),
			page: (options?.page ?? DEFAULT_PAGE).toString()
		});
		const r = await fetch(`${url.toString()}?${params.toString()}`, {
			method: 'GET',
			headers: { Authorization: `Bearer ${token}` }
		});

		if (r.ok) {
			return r.json() as Promise<Paginated<Station>>;
		} else {
			throw new Error(r.statusText);
		}
	},

	async create(body: Station, token: string): Promise<Station> {
		const url = new URL(TRAIN_API_BASE_URL + '/stations/');
		const r = await fetch(`${url.toString()}`, {
			method: 'POST',
			headers: { authorization: `Bearer ${token}`, 'content-type': 'application/json' },
			body: JSON.stringify(body)
		});

		if (r.ok) {
			return r.json() as Promise<Station>;
		} else {
			throw new Error(r.statusText);
		}
	},

	async findById(id: string, token: string): Promise<Station> {
		const url = new URL(TRAIN_API_BASE_URL + `/stations/${id}`);
		const r = await fetch(`${url.toString()}`, {
			method: 'GET',
			headers: { authorization: `Bearer ${token}` }
		});

		if (r.ok) {
			return r.json() as Promise<Station>;
		} else {
			throw new Error(r.statusText);
		}
	},

	async makeFavoriteById(
		id: string,
		body: { is_favorite: boolean },
		token: string
	): Promise<Station> {
		const url = new URL(TRAIN_API_BASE_URL + `/stations/${id}/makeFavorite`);
		const r = await fetch(`${url.toString()}`, {
			method: 'POST',
			headers: { authorization: `Bearer ${token}`, 'content-type': 'application/json' },
			body: JSON.stringify(body)
		});

		if (r.ok) {
			return r.json() as Promise<Station>;
		} else {
			throw new Error(r.statusText);
		}
	}
};
export default StationRepository;
