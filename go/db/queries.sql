-- name: FindOneStationAndFavorite :one
SELECT sqlc.embed(stations), CAST(favorites.user_id IS NOT NULL AS BOOL) AS favorite
FROM stations
LEFT OUTER JOIN favorites on favorites.station_id = stations.id AND favorites.user_id = @user_id
WHERE id = @id
LIMIT 1;

-- name: FindManyStationAndFavorite :many
SELECT sqlc.embed(stations), CAST(favorites.user_id IS NOT NULL AS BOOL) AS favorite
FROM stations
LEFT OUTER JOIN favorites on favorites.station_id = stations.id AND favorites.user_id = @user_id
WHERE libelle LIKE '%'||@search||'%' OR @search = ''
ORDER BY libelle
LIMIT @limit
OFFSET (@page - 1) * @limit;

-- name: CreateStation :exec
INSERT INTO stations (
  id,
  commune,
  y_wgs84,
  x_wgs84,
  libelle,
  idgaia,
  voyageurs,
  geo_point_2d,
  code_ligne,
  x_l93,
  c_geo,
  rg_troncon,
  geo_shape,
  pk,
  idreseau,
  departemen,
  y_l93,
  fret
) VALUES (
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?,
  ?
);

-- name: CountStations :one
SELECT count(*) FROM stations WHERE libelle LIKE '%'||@search||'%' OR @search = '';

-- name: CountFavorites :one
SELECT count(*) FROM favorites;

-- name: CreateFavorite :exec
INSERT INTO favorites (
  station_id,
  user_id
) VALUES (
  ?,
  ?
);

-- name: DeleteFavorite :exec
DELETE FROM favorites WHERE station_id = ? AND user_id = ?;

-- name: DeleteAllFavorites :exec
DELETE FROM favorites;

-- name: DeleteAllStations :exec
DELETE FROM stations;
