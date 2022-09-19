import type { PageLoad } from './$types';

export const load: PageLoad = ({ url }) => {
	let initialPageNumber = 1;
	const queryPage = url.searchParams.get('page');
	if (queryPage !== null) {
		initialPageNumber = parseInt(queryPage);
	}
	const initialSearchQuery = url.searchParams.get('s') ?? '';
	return {
		initialSearchQuery: initialSearchQuery,
		initialPageNumber: initialPageNumber
	};
};
