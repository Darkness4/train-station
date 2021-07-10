import type { Fields } from '$lib/entities/fields';
import type { Geometry } from '$lib/entities/geometry';

export interface Station {
	recordid: string;
	datasetid: string;
	is_favorite: boolean;
	libelle: string;
	fields?: Fields;
	geometry?: Geometry;
	record_timestamp: string;
}
