PRAGMA foreign_keys = ON;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS stations (
  id VARCHAR(255) NOT NULL PRIMARY KEY,
  commune VARCHAR(255) NOT NULL,
  y_wgs84 DOUBLE NOT NULL,
  x_wgs84 DOUBLE NOT NULL,
  libelle VARCHAR(255) NOT NULL,
  idgaia VARCHAR(255) NOT NULL,
  voyageurs VARCHAR(255) NOT NULL,
  geo_point_2d VARCHAR(255) NOT NULL,
  code_ligne VARCHAR(255) NOT NULL,
  x_l93 DOUBLE NOT NULL,
  c_geo VARCHAR(255) NOT NULL,
  rg_troncon BIGINT NOT NULL,
  geo_shape VARCHAR(255) NOT NULL,
  pk VARCHAR(255) NOT NULL,
  idreseau BIGINT NOT NULL,
  departemen VARCHAR(255) NOT NULL,
  y_l93 DOUBLE NOT NULL,
  fret VARCHAR(255) NOT NULL
);
CREATE INDEX idx_libelle ON stations(libelle);
CREATE TABLE IF NOT EXISTS favorites (
  station_id VARCHAR(255) NOT NULL,
  user_id VARCHAR(255) NOT NULL CHECK(user_id <> ''),
  PRIMARY KEY(station_id, user_id),
  FOREIGN KEY(station_id) REFERENCES stations(id)
);
COMMIT;
