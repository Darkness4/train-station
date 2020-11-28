import { Fields } from 'entities/fields';
import { Geometry } from 'entities/geometry';

export interface StationModel {
  recordid: string;
  datasetid: string;
  is_favorite: boolean;
  fields?: Fields;
  geometry?: Geometry;
  record_timestamp?: string;
}
