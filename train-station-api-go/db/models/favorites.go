// Code generated by SQLBoiler 4.14.0 (https://github.com/volatiletech/sqlboiler). DO NOT EDIT.
// This file is meant to be re-generated in place and/or deleted at any time.

package models

import (
	"context"
	"database/sql"
	"fmt"
	"reflect"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/friendsofgo/errors"
	"github.com/volatiletech/sqlboiler/v4/boil"
	"github.com/volatiletech/sqlboiler/v4/queries"
	"github.com/volatiletech/sqlboiler/v4/queries/qm"
	"github.com/volatiletech/sqlboiler/v4/queries/qmhelper"
	"github.com/volatiletech/strmangle"
)

// Favorite is an object representing the database table.
type Favorite struct {
	StationID string `boil:"station_id" json:"station_id" toml:"station_id" yaml:"station_id"`
	UserID    string `boil:"user_id" json:"user_id" toml:"user_id" yaml:"user_id"`

	R *favoriteR `boil:"-" json:"-" toml:"-" yaml:"-"`
	L favoriteL  `boil:"-" json:"-" toml:"-" yaml:"-"`
}

var FavoriteColumns = struct {
	StationID string
	UserID    string
}{
	StationID: "station_id",
	UserID:    "user_id",
}

var FavoriteTableColumns = struct {
	StationID string
	UserID    string
}{
	StationID: "favorites.station_id",
	UserID:    "favorites.user_id",
}

// Generated where

type whereHelperstring struct{ field string }

func (w whereHelperstring) EQ(x string) qm.QueryMod  { return qmhelper.Where(w.field, qmhelper.EQ, x) }
func (w whereHelperstring) NEQ(x string) qm.QueryMod { return qmhelper.Where(w.field, qmhelper.NEQ, x) }
func (w whereHelperstring) LT(x string) qm.QueryMod  { return qmhelper.Where(w.field, qmhelper.LT, x) }
func (w whereHelperstring) LTE(x string) qm.QueryMod { return qmhelper.Where(w.field, qmhelper.LTE, x) }
func (w whereHelperstring) GT(x string) qm.QueryMod  { return qmhelper.Where(w.field, qmhelper.GT, x) }
func (w whereHelperstring) GTE(x string) qm.QueryMod { return qmhelper.Where(w.field, qmhelper.GTE, x) }
func (w whereHelperstring) IN(slice []string) qm.QueryMod {
	values := make([]interface{}, 0, len(slice))
	for _, value := range slice {
		values = append(values, value)
	}
	return qm.WhereIn(fmt.Sprintf("%s IN ?", w.field), values...)
}
func (w whereHelperstring) NIN(slice []string) qm.QueryMod {
	values := make([]interface{}, 0, len(slice))
	for _, value := range slice {
		values = append(values, value)
	}
	return qm.WhereNotIn(fmt.Sprintf("%s NOT IN ?", w.field), values...)
}

var FavoriteWhere = struct {
	StationID whereHelperstring
	UserID    whereHelperstring
}{
	StationID: whereHelperstring{field: "\"favorites\".\"station_id\""},
	UserID:    whereHelperstring{field: "\"favorites\".\"user_id\""},
}

// FavoriteRels is where relationship names are stored.
var FavoriteRels = struct {
	Station string
}{
	Station: "Station",
}

// favoriteR is where relationships are stored.
type favoriteR struct {
	Station *Station `boil:"Station" json:"Station" toml:"Station" yaml:"Station"`
}

// NewStruct creates a new relationship struct
func (*favoriteR) NewStruct() *favoriteR {
	return &favoriteR{}
}

func (r *favoriteR) GetStation() *Station {
	if r == nil {
		return nil
	}
	return r.Station
}

// favoriteL is where Load methods for each relationship are stored.
type favoriteL struct{}

var (
	favoriteAllColumns            = []string{"station_id", "user_id"}
	favoriteColumnsWithoutDefault = []string{"station_id", "user_id"}
	favoriteColumnsWithDefault    = []string{}
	favoritePrimaryKeyColumns     = []string{"station_id", "user_id"}
	favoriteGeneratedColumns      = []string{}
)

type (
	// FavoriteSlice is an alias for a slice of pointers to Favorite.
	// This should almost always be used instead of []Favorite.
	FavoriteSlice []*Favorite
	// FavoriteHook is the signature for custom Favorite hook methods
	FavoriteHook func(context.Context, boil.ContextExecutor, *Favorite) error

	favoriteQuery struct {
		*queries.Query
	}
)

// Cache for insert, update and upsert
var (
	favoriteType                 = reflect.TypeOf(&Favorite{})
	favoriteMapping              = queries.MakeStructMapping(favoriteType)
	favoritePrimaryKeyMapping, _ = queries.BindMapping(favoriteType, favoriteMapping, favoritePrimaryKeyColumns)
	favoriteInsertCacheMut       sync.RWMutex
	favoriteInsertCache          = make(map[string]insertCache)
	favoriteUpdateCacheMut       sync.RWMutex
	favoriteUpdateCache          = make(map[string]updateCache)
	favoriteUpsertCacheMut       sync.RWMutex
	favoriteUpsertCache          = make(map[string]insertCache)
)

var (
	// Force time package dependency for automated UpdatedAt/CreatedAt.
	_ = time.Second
	// Force qmhelper dependency for where clause generation (which doesn't
	// always happen)
	_ = qmhelper.Where
)

var favoriteAfterSelectHooks []FavoriteHook

var favoriteBeforeInsertHooks []FavoriteHook
var favoriteAfterInsertHooks []FavoriteHook

var favoriteBeforeUpdateHooks []FavoriteHook
var favoriteAfterUpdateHooks []FavoriteHook

var favoriteBeforeDeleteHooks []FavoriteHook
var favoriteAfterDeleteHooks []FavoriteHook

var favoriteBeforeUpsertHooks []FavoriteHook
var favoriteAfterUpsertHooks []FavoriteHook

// doAfterSelectHooks executes all "after Select" hooks.
func (o *Favorite) doAfterSelectHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteAfterSelectHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doBeforeInsertHooks executes all "before insert" hooks.
func (o *Favorite) doBeforeInsertHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteBeforeInsertHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doAfterInsertHooks executes all "after Insert" hooks.
func (o *Favorite) doAfterInsertHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteAfterInsertHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doBeforeUpdateHooks executes all "before Update" hooks.
func (o *Favorite) doBeforeUpdateHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteBeforeUpdateHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doAfterUpdateHooks executes all "after Update" hooks.
func (o *Favorite) doAfterUpdateHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteAfterUpdateHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doBeforeDeleteHooks executes all "before Delete" hooks.
func (o *Favorite) doBeforeDeleteHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteBeforeDeleteHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doAfterDeleteHooks executes all "after Delete" hooks.
func (o *Favorite) doAfterDeleteHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteAfterDeleteHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doBeforeUpsertHooks executes all "before Upsert" hooks.
func (o *Favorite) doBeforeUpsertHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteBeforeUpsertHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// doAfterUpsertHooks executes all "after Upsert" hooks.
func (o *Favorite) doAfterUpsertHooks(ctx context.Context, exec boil.ContextExecutor) (err error) {
	if boil.HooksAreSkipped(ctx) {
		return nil
	}

	for _, hook := range favoriteAfterUpsertHooks {
		if err := hook(ctx, exec, o); err != nil {
			return err
		}
	}

	return nil
}

// AddFavoriteHook registers your hook function for all future operations.
func AddFavoriteHook(hookPoint boil.HookPoint, favoriteHook FavoriteHook) {
	switch hookPoint {
	case boil.AfterSelectHook:
		favoriteAfterSelectHooks = append(favoriteAfterSelectHooks, favoriteHook)
	case boil.BeforeInsertHook:
		favoriteBeforeInsertHooks = append(favoriteBeforeInsertHooks, favoriteHook)
	case boil.AfterInsertHook:
		favoriteAfterInsertHooks = append(favoriteAfterInsertHooks, favoriteHook)
	case boil.BeforeUpdateHook:
		favoriteBeforeUpdateHooks = append(favoriteBeforeUpdateHooks, favoriteHook)
	case boil.AfterUpdateHook:
		favoriteAfterUpdateHooks = append(favoriteAfterUpdateHooks, favoriteHook)
	case boil.BeforeDeleteHook:
		favoriteBeforeDeleteHooks = append(favoriteBeforeDeleteHooks, favoriteHook)
	case boil.AfterDeleteHook:
		favoriteAfterDeleteHooks = append(favoriteAfterDeleteHooks, favoriteHook)
	case boil.BeforeUpsertHook:
		favoriteBeforeUpsertHooks = append(favoriteBeforeUpsertHooks, favoriteHook)
	case boil.AfterUpsertHook:
		favoriteAfterUpsertHooks = append(favoriteAfterUpsertHooks, favoriteHook)
	}
}

// One returns a single favorite record from the query.
func (q favoriteQuery) One(ctx context.Context, exec boil.ContextExecutor) (*Favorite, error) {
	o := &Favorite{}

	queries.SetLimit(q.Query, 1)

	err := q.Bind(ctx, exec, o)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, sql.ErrNoRows
		}
		return nil, errors.Wrap(err, "models: failed to execute a one query for favorites")
	}

	if err := o.doAfterSelectHooks(ctx, exec); err != nil {
		return o, err
	}

	return o, nil
}

// All returns all Favorite records from the query.
func (q favoriteQuery) All(ctx context.Context, exec boil.ContextExecutor) (FavoriteSlice, error) {
	var o []*Favorite

	err := q.Bind(ctx, exec, &o)
	if err != nil {
		return nil, errors.Wrap(err, "models: failed to assign all query results to Favorite slice")
	}

	if len(favoriteAfterSelectHooks) != 0 {
		for _, obj := range o {
			if err := obj.doAfterSelectHooks(ctx, exec); err != nil {
				return o, err
			}
		}
	}

	return o, nil
}

// Count returns the count of all Favorite records in the query.
func (q favoriteQuery) Count(ctx context.Context, exec boil.ContextExecutor) (int64, error) {
	var count int64

	queries.SetSelect(q.Query, nil)
	queries.SetCount(q.Query)

	err := q.Query.QueryRowContext(ctx, exec).Scan(&count)
	if err != nil {
		return 0, errors.Wrap(err, "models: failed to count favorites rows")
	}

	return count, nil
}

// Exists checks if the row exists in the table.
func (q favoriteQuery) Exists(ctx context.Context, exec boil.ContextExecutor) (bool, error) {
	var count int64

	queries.SetSelect(q.Query, nil)
	queries.SetCount(q.Query)
	queries.SetLimit(q.Query, 1)

	err := q.Query.QueryRowContext(ctx, exec).Scan(&count)
	if err != nil {
		return false, errors.Wrap(err, "models: failed to check if favorites exists")
	}

	return count > 0, nil
}

// Station pointed to by the foreign key.
func (o *Favorite) Station(mods ...qm.QueryMod) stationQuery {
	queryMods := []qm.QueryMod{
		qm.Where("\"id\" = ?", o.StationID),
	}

	queryMods = append(queryMods, mods...)

	return Stations(queryMods...)
}

// LoadStation allows an eager lookup of values, cached into the
// loaded structs of the objects. This is for an N-1 relationship.
func (favoriteL) LoadStation(ctx context.Context, e boil.ContextExecutor, singular bool, maybeFavorite interface{}, mods queries.Applicator) error {
	var slice []*Favorite
	var object *Favorite

	if singular {
		var ok bool
		object, ok = maybeFavorite.(*Favorite)
		if !ok {
			object = new(Favorite)
			ok = queries.SetFromEmbeddedStruct(&object, &maybeFavorite)
			if !ok {
				return errors.New(fmt.Sprintf("failed to set %T from embedded struct %T", object, maybeFavorite))
			}
		}
	} else {
		s, ok := maybeFavorite.(*[]*Favorite)
		if ok {
			slice = *s
		} else {
			ok = queries.SetFromEmbeddedStruct(&slice, maybeFavorite)
			if !ok {
				return errors.New(fmt.Sprintf("failed to set %T from embedded struct %T", slice, maybeFavorite))
			}
		}
	}

	args := make([]interface{}, 0, 1)
	if singular {
		if object.R == nil {
			object.R = &favoriteR{}
		}
		args = append(args, object.StationID)

	} else {
	Outer:
		for _, obj := range slice {
			if obj.R == nil {
				obj.R = &favoriteR{}
			}

			for _, a := range args {
				if a == obj.StationID {
					continue Outer
				}
			}

			args = append(args, obj.StationID)

		}
	}

	if len(args) == 0 {
		return nil
	}

	query := NewQuery(
		qm.From(`stations`),
		qm.WhereIn(`stations.id in ?`, args...),
	)
	if mods != nil {
		mods.Apply(query)
	}

	results, err := query.QueryContext(ctx, e)
	if err != nil {
		return errors.Wrap(err, "failed to eager load Station")
	}

	var resultSlice []*Station
	if err = queries.Bind(results, &resultSlice); err != nil {
		return errors.Wrap(err, "failed to bind eager loaded slice Station")
	}

	if err = results.Close(); err != nil {
		return errors.Wrap(err, "failed to close results of eager load for stations")
	}
	if err = results.Err(); err != nil {
		return errors.Wrap(err, "error occurred during iteration of eager loaded relations for stations")
	}

	if len(stationAfterSelectHooks) != 0 {
		for _, obj := range resultSlice {
			if err := obj.doAfterSelectHooks(ctx, e); err != nil {
				return err
			}
		}
	}

	if len(resultSlice) == 0 {
		return nil
	}

	if singular {
		foreign := resultSlice[0]
		object.R.Station = foreign
		if foreign.R == nil {
			foreign.R = &stationR{}
		}
		foreign.R.Favorites = append(foreign.R.Favorites, object)
		return nil
	}

	for _, local := range slice {
		for _, foreign := range resultSlice {
			if local.StationID == foreign.ID {
				local.R.Station = foreign
				if foreign.R == nil {
					foreign.R = &stationR{}
				}
				foreign.R.Favorites = append(foreign.R.Favorites, local)
				break
			}
		}
	}

	return nil
}

// SetStation of the favorite to the related item.
// Sets o.R.Station to related.
// Adds o to related.R.Favorites.
func (o *Favorite) SetStation(ctx context.Context, exec boil.ContextExecutor, insert bool, related *Station) error {
	var err error
	if insert {
		if err = related.Insert(ctx, exec, boil.Infer()); err != nil {
			return errors.Wrap(err, "failed to insert into foreign table")
		}
	}

	updateQuery := fmt.Sprintf(
		"UPDATE \"favorites\" SET %s WHERE %s",
		strmangle.SetParamNames("\"", "\"", 0, []string{"station_id"}),
		strmangle.WhereClause("\"", "\"", 0, favoritePrimaryKeyColumns),
	)
	values := []interface{}{related.ID, o.StationID, o.UserID}

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, updateQuery)
		fmt.Fprintln(writer, values)
	}
	if _, err = exec.ExecContext(ctx, updateQuery, values...); err != nil {
		return errors.Wrap(err, "failed to update local table")
	}

	o.StationID = related.ID
	if o.R == nil {
		o.R = &favoriteR{
			Station: related,
		}
	} else {
		o.R.Station = related
	}

	if related.R == nil {
		related.R = &stationR{
			Favorites: FavoriteSlice{o},
		}
	} else {
		related.R.Favorites = append(related.R.Favorites, o)
	}

	return nil
}

// Favorites retrieves all the records using an executor.
func Favorites(mods ...qm.QueryMod) favoriteQuery {
	mods = append(mods, qm.From("\"favorites\""))
	q := NewQuery(mods...)
	if len(queries.GetSelect(q)) == 0 {
		queries.SetSelect(q, []string{"\"favorites\".*"})
	}

	return favoriteQuery{q}
}

// FindFavorite retrieves a single record by ID with an executor.
// If selectCols is empty Find will return all columns.
func FindFavorite(ctx context.Context, exec boil.ContextExecutor, stationID string, userID string, selectCols ...string) (*Favorite, error) {
	favoriteObj := &Favorite{}

	sel := "*"
	if len(selectCols) > 0 {
		sel = strings.Join(strmangle.IdentQuoteSlice(dialect.LQ, dialect.RQ, selectCols), ",")
	}
	query := fmt.Sprintf(
		"select %s from \"favorites\" where \"station_id\"=? AND \"user_id\"=?", sel,
	)

	q := queries.Raw(query, stationID, userID)

	err := q.Bind(ctx, exec, favoriteObj)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, sql.ErrNoRows
		}
		return nil, errors.Wrap(err, "models: unable to select from favorites")
	}

	if err = favoriteObj.doAfterSelectHooks(ctx, exec); err != nil {
		return favoriteObj, err
	}

	return favoriteObj, nil
}

// Insert a single record using an executor.
// See boil.Columns.InsertColumnSet documentation to understand column list inference for inserts.
func (o *Favorite) Insert(ctx context.Context, exec boil.ContextExecutor, columns boil.Columns) error {
	if o == nil {
		return errors.New("models: no favorites provided for insertion")
	}

	var err error

	if err := o.doBeforeInsertHooks(ctx, exec); err != nil {
		return err
	}

	nzDefaults := queries.NonZeroDefaultSet(favoriteColumnsWithDefault, o)

	key := makeCacheKey(columns, nzDefaults)
	favoriteInsertCacheMut.RLock()
	cache, cached := favoriteInsertCache[key]
	favoriteInsertCacheMut.RUnlock()

	if !cached {
		wl, returnColumns := columns.InsertColumnSet(
			favoriteAllColumns,
			favoriteColumnsWithDefault,
			favoriteColumnsWithoutDefault,
			nzDefaults,
		)

		cache.valueMapping, err = queries.BindMapping(favoriteType, favoriteMapping, wl)
		if err != nil {
			return err
		}
		cache.retMapping, err = queries.BindMapping(favoriteType, favoriteMapping, returnColumns)
		if err != nil {
			return err
		}
		if len(wl) != 0 {
			cache.query = fmt.Sprintf("INSERT INTO \"favorites\" (\"%s\") %%sVALUES (%s)%%s", strings.Join(wl, "\",\""), strmangle.Placeholders(dialect.UseIndexPlaceholders, len(wl), 1, 1))
		} else {
			cache.query = "INSERT INTO \"favorites\" %sDEFAULT VALUES%s"
		}

		var queryOutput, queryReturning string

		if len(cache.retMapping) != 0 {
			queryReturning = fmt.Sprintf(" RETURNING \"%s\"", strings.Join(returnColumns, "\",\""))
		}

		cache.query = fmt.Sprintf(cache.query, queryOutput, queryReturning)
	}

	value := reflect.Indirect(reflect.ValueOf(o))
	vals := queries.ValuesFromMapping(value, cache.valueMapping)

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, cache.query)
		fmt.Fprintln(writer, vals)
	}

	if len(cache.retMapping) != 0 {
		err = exec.QueryRowContext(ctx, cache.query, vals...).Scan(queries.PtrsFromMapping(value, cache.retMapping)...)
	} else {
		_, err = exec.ExecContext(ctx, cache.query, vals...)
	}

	if err != nil {
		return errors.Wrap(err, "models: unable to insert into favorites")
	}

	if !cached {
		favoriteInsertCacheMut.Lock()
		favoriteInsertCache[key] = cache
		favoriteInsertCacheMut.Unlock()
	}

	return o.doAfterInsertHooks(ctx, exec)
}

// Update uses an executor to update the Favorite.
// See boil.Columns.UpdateColumnSet documentation to understand column list inference for updates.
// Update does not automatically update the record in case of default values. Use .Reload() to refresh the records.
func (o *Favorite) Update(ctx context.Context, exec boil.ContextExecutor, columns boil.Columns) (int64, error) {
	var err error
	if err = o.doBeforeUpdateHooks(ctx, exec); err != nil {
		return 0, err
	}
	key := makeCacheKey(columns, nil)
	favoriteUpdateCacheMut.RLock()
	cache, cached := favoriteUpdateCache[key]
	favoriteUpdateCacheMut.RUnlock()

	if !cached {
		wl := columns.UpdateColumnSet(
			favoriteAllColumns,
			favoritePrimaryKeyColumns,
		)

		if !columns.IsWhitelist() {
			wl = strmangle.SetComplement(wl, []string{"created_at"})
		}
		if len(wl) == 0 {
			return 0, errors.New("models: unable to update favorites, could not build whitelist")
		}

		cache.query = fmt.Sprintf("UPDATE \"favorites\" SET %s WHERE %s",
			strmangle.SetParamNames("\"", "\"", 0, wl),
			strmangle.WhereClause("\"", "\"", 0, favoritePrimaryKeyColumns),
		)
		cache.valueMapping, err = queries.BindMapping(favoriteType, favoriteMapping, append(wl, favoritePrimaryKeyColumns...))
		if err != nil {
			return 0, err
		}
	}

	values := queries.ValuesFromMapping(reflect.Indirect(reflect.ValueOf(o)), cache.valueMapping)

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, cache.query)
		fmt.Fprintln(writer, values)
	}
	var result sql.Result
	result, err = exec.ExecContext(ctx, cache.query, values...)
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to update favorites row")
	}

	rowsAff, err := result.RowsAffected()
	if err != nil {
		return 0, errors.Wrap(err, "models: failed to get rows affected by update for favorites")
	}

	if !cached {
		favoriteUpdateCacheMut.Lock()
		favoriteUpdateCache[key] = cache
		favoriteUpdateCacheMut.Unlock()
	}

	return rowsAff, o.doAfterUpdateHooks(ctx, exec)
}

// UpdateAll updates all rows with the specified column values.
func (q favoriteQuery) UpdateAll(ctx context.Context, exec boil.ContextExecutor, cols M) (int64, error) {
	queries.SetUpdate(q.Query, cols)

	result, err := q.Query.ExecContext(ctx, exec)
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to update all for favorites")
	}

	rowsAff, err := result.RowsAffected()
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to retrieve rows affected for favorites")
	}

	return rowsAff, nil
}

// UpdateAll updates all rows with the specified column values, using an executor.
func (o FavoriteSlice) UpdateAll(ctx context.Context, exec boil.ContextExecutor, cols M) (int64, error) {
	ln := int64(len(o))
	if ln == 0 {
		return 0, nil
	}

	if len(cols) == 0 {
		return 0, errors.New("models: update all requires at least one column argument")
	}

	colNames := make([]string, len(cols))
	args := make([]interface{}, len(cols))

	i := 0
	for name, value := range cols {
		colNames[i] = name
		args[i] = value
		i++
	}

	// Append all of the primary key values for each column
	for _, obj := range o {
		pkeyArgs := queries.ValuesFromMapping(reflect.Indirect(reflect.ValueOf(obj)), favoritePrimaryKeyMapping)
		args = append(args, pkeyArgs...)
	}

	sql := fmt.Sprintf("UPDATE \"favorites\" SET %s WHERE %s",
		strmangle.SetParamNames("\"", "\"", 0, colNames),
		strmangle.WhereClauseRepeated(string(dialect.LQ), string(dialect.RQ), 0, favoritePrimaryKeyColumns, len(o)))

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, sql)
		fmt.Fprintln(writer, args...)
	}
	result, err := exec.ExecContext(ctx, sql, args...)
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to update all in favorite slice")
	}

	rowsAff, err := result.RowsAffected()
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to retrieve rows affected all in update all favorite")
	}
	return rowsAff, nil
}

// Upsert attempts an insert using an executor, and does an update or ignore on conflict.
// See boil.Columns documentation for how to properly use updateColumns and insertColumns.
func (o *Favorite) Upsert(ctx context.Context, exec boil.ContextExecutor, updateOnConflict bool, conflictColumns []string, updateColumns, insertColumns boil.Columns) error {
	if o == nil {
		return errors.New("models: no favorites provided for upsert")
	}

	if err := o.doBeforeUpsertHooks(ctx, exec); err != nil {
		return err
	}

	nzDefaults := queries.NonZeroDefaultSet(favoriteColumnsWithDefault, o)

	// Build cache key in-line uglily - mysql vs psql problems
	buf := strmangle.GetBuffer()
	if updateOnConflict {
		buf.WriteByte('t')
	} else {
		buf.WriteByte('f')
	}
	buf.WriteByte('.')
	for _, c := range conflictColumns {
		buf.WriteString(c)
	}
	buf.WriteByte('.')
	buf.WriteString(strconv.Itoa(updateColumns.Kind))
	for _, c := range updateColumns.Cols {
		buf.WriteString(c)
	}
	buf.WriteByte('.')
	buf.WriteString(strconv.Itoa(insertColumns.Kind))
	for _, c := range insertColumns.Cols {
		buf.WriteString(c)
	}
	buf.WriteByte('.')
	for _, c := range nzDefaults {
		buf.WriteString(c)
	}
	key := buf.String()
	strmangle.PutBuffer(buf)

	favoriteUpsertCacheMut.RLock()
	cache, cached := favoriteUpsertCache[key]
	favoriteUpsertCacheMut.RUnlock()

	var err error

	if !cached {
		insert, ret := insertColumns.InsertColumnSet(
			favoriteAllColumns,
			favoriteColumnsWithDefault,
			favoriteColumnsWithoutDefault,
			nzDefaults,
		)
		update := updateColumns.UpdateColumnSet(
			favoriteAllColumns,
			favoritePrimaryKeyColumns,
		)

		if updateOnConflict && len(update) == 0 {
			return errors.New("models: unable to upsert favorites, could not build update column list")
		}

		conflict := conflictColumns
		if len(conflict) == 0 {
			conflict = make([]string, len(favoritePrimaryKeyColumns))
			copy(conflict, favoritePrimaryKeyColumns)
		}
		cache.query = buildUpsertQuerySQLite(dialect, "\"favorites\"", updateOnConflict, ret, update, conflict, insert)

		cache.valueMapping, err = queries.BindMapping(favoriteType, favoriteMapping, insert)
		if err != nil {
			return err
		}
		if len(ret) != 0 {
			cache.retMapping, err = queries.BindMapping(favoriteType, favoriteMapping, ret)
			if err != nil {
				return err
			}
		}
	}

	value := reflect.Indirect(reflect.ValueOf(o))
	vals := queries.ValuesFromMapping(value, cache.valueMapping)
	var returns []interface{}
	if len(cache.retMapping) != 0 {
		returns = queries.PtrsFromMapping(value, cache.retMapping)
	}

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, cache.query)
		fmt.Fprintln(writer, vals)
	}
	if len(cache.retMapping) != 0 {
		err = exec.QueryRowContext(ctx, cache.query, vals...).Scan(returns...)
		if errors.Is(err, sql.ErrNoRows) {
			err = nil // Postgres doesn't return anything when there's no update
		}
	} else {
		_, err = exec.ExecContext(ctx, cache.query, vals...)
	}
	if err != nil {
		return errors.Wrap(err, "models: unable to upsert favorites")
	}

	if !cached {
		favoriteUpsertCacheMut.Lock()
		favoriteUpsertCache[key] = cache
		favoriteUpsertCacheMut.Unlock()
	}

	return o.doAfterUpsertHooks(ctx, exec)
}

// Delete deletes a single Favorite record with an executor.
// Delete will match against the primary key column to find the record to delete.
func (o *Favorite) Delete(ctx context.Context, exec boil.ContextExecutor) (int64, error) {
	if o == nil {
		return 0, errors.New("models: no Favorite provided for delete")
	}

	if err := o.doBeforeDeleteHooks(ctx, exec); err != nil {
		return 0, err
	}

	args := queries.ValuesFromMapping(reflect.Indirect(reflect.ValueOf(o)), favoritePrimaryKeyMapping)
	sql := "DELETE FROM \"favorites\" WHERE \"station_id\"=? AND \"user_id\"=?"

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, sql)
		fmt.Fprintln(writer, args...)
	}
	result, err := exec.ExecContext(ctx, sql, args...)
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to delete from favorites")
	}

	rowsAff, err := result.RowsAffected()
	if err != nil {
		return 0, errors.Wrap(err, "models: failed to get rows affected by delete for favorites")
	}

	if err := o.doAfterDeleteHooks(ctx, exec); err != nil {
		return 0, err
	}

	return rowsAff, nil
}

// DeleteAll deletes all matching rows.
func (q favoriteQuery) DeleteAll(ctx context.Context, exec boil.ContextExecutor) (int64, error) {
	if q.Query == nil {
		return 0, errors.New("models: no favoriteQuery provided for delete all")
	}

	queries.SetDelete(q.Query)

	result, err := q.Query.ExecContext(ctx, exec)
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to delete all from favorites")
	}

	rowsAff, err := result.RowsAffected()
	if err != nil {
		return 0, errors.Wrap(err, "models: failed to get rows affected by deleteall for favorites")
	}

	return rowsAff, nil
}

// DeleteAll deletes all rows in the slice, using an executor.
func (o FavoriteSlice) DeleteAll(ctx context.Context, exec boil.ContextExecutor) (int64, error) {
	if len(o) == 0 {
		return 0, nil
	}

	if len(favoriteBeforeDeleteHooks) != 0 {
		for _, obj := range o {
			if err := obj.doBeforeDeleteHooks(ctx, exec); err != nil {
				return 0, err
			}
		}
	}

	var args []interface{}
	for _, obj := range o {
		pkeyArgs := queries.ValuesFromMapping(reflect.Indirect(reflect.ValueOf(obj)), favoritePrimaryKeyMapping)
		args = append(args, pkeyArgs...)
	}

	sql := "DELETE FROM \"favorites\" WHERE " +
		strmangle.WhereClauseRepeated(string(dialect.LQ), string(dialect.RQ), 0, favoritePrimaryKeyColumns, len(o))

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, sql)
		fmt.Fprintln(writer, args)
	}
	result, err := exec.ExecContext(ctx, sql, args...)
	if err != nil {
		return 0, errors.Wrap(err, "models: unable to delete all from favorite slice")
	}

	rowsAff, err := result.RowsAffected()
	if err != nil {
		return 0, errors.Wrap(err, "models: failed to get rows affected by deleteall for favorites")
	}

	if len(favoriteAfterDeleteHooks) != 0 {
		for _, obj := range o {
			if err := obj.doAfterDeleteHooks(ctx, exec); err != nil {
				return 0, err
			}
		}
	}

	return rowsAff, nil
}

// Reload refetches the object from the database
// using the primary keys with an executor.
func (o *Favorite) Reload(ctx context.Context, exec boil.ContextExecutor) error {
	ret, err := FindFavorite(ctx, exec, o.StationID, o.UserID)
	if err != nil {
		return err
	}

	*o = *ret
	return nil
}

// ReloadAll refetches every row with matching primary key column values
// and overwrites the original object slice with the newly updated slice.
func (o *FavoriteSlice) ReloadAll(ctx context.Context, exec boil.ContextExecutor) error {
	if o == nil || len(*o) == 0 {
		return nil
	}

	slice := FavoriteSlice{}
	var args []interface{}
	for _, obj := range *o {
		pkeyArgs := queries.ValuesFromMapping(reflect.Indirect(reflect.ValueOf(obj)), favoritePrimaryKeyMapping)
		args = append(args, pkeyArgs...)
	}

	sql := "SELECT \"favorites\".* FROM \"favorites\" WHERE " +
		strmangle.WhereClauseRepeated(string(dialect.LQ), string(dialect.RQ), 0, favoritePrimaryKeyColumns, len(*o))

	q := queries.Raw(sql, args...)

	err := q.Bind(ctx, exec, &slice)
	if err != nil {
		return errors.Wrap(err, "models: unable to reload all in FavoriteSlice")
	}

	*o = slice

	return nil
}

// FavoriteExists checks if the Favorite row exists.
func FavoriteExists(ctx context.Context, exec boil.ContextExecutor, stationID string, userID string) (bool, error) {
	var exists bool
	sql := "select exists(select 1 from \"favorites\" where \"station_id\"=? AND \"user_id\"=? limit 1)"

	if boil.IsDebug(ctx) {
		writer := boil.DebugWriterFrom(ctx)
		fmt.Fprintln(writer, sql)
		fmt.Fprintln(writer, stationID, userID)
	}
	row := exec.QueryRowContext(ctx, sql, stationID, userID)

	err := row.Scan(&exists)
	if err != nil {
		return false, errors.Wrap(err, "models: unable to check if favorites exists")
	}

	return exists, nil
}

// Exists checks if the Favorite row exists.
func (o *Favorite) Exists(ctx context.Context, exec boil.ContextExecutor) (bool, error) {
	return FavoriteExists(ctx, exec, o.StationID, o.UserID)
}
