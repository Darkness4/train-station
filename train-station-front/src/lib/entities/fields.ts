import type { Geometry } from '$lib/entities/geometry';

export interface Fields {
	id: number;
	commune: string;
	y_wgs84: number;
	x_wgs84: number;
	libelle: string;
	idgaia: string;
	voyageurs: string;
	geo_point_2d: number[];
	code_ligne: string;
	x_l93: number;
	c_geo: number[];
	rg_troncon: number;
	geo_shape: Geometry;
	pk: string;
	idreseau: number;
	departemen: string;
	y_l93: number;
	fret: string;
}
