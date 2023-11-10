package com.example.trainstationapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trainstationapp.domain.entities.Station
import kotlinx.coroutines.flow.Flow

/** Cache for storing the `StationModel`. */
@Dao
interface StationDao {
    /** Observe the whole cache */
    @Query("SELECT * FROM stations") fun watch(): Flow<List<Station>>

    /** Observe one item from the cache */
    @Query("SELECT * FROM stations WHERE id = :id") fun watchById(id: String): Flow<Station>

    /** Fetch a paging source from cache, with a search filters. */
    @Query("SELECT * FROM stations WHERE libelle LIKE '%' || :search || '%' ORDER BY libelle, id")
    fun watchAsPagingSource(search: String): PagingSource<Int, Station>

    /** Fetch a paging source from cache. */
    @Query("SELECT * FROM stations ORDER BY libelle, id")
    fun watchAsPagingSource(): PagingSource<Int, Station>

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(items: List<Station>)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(vararg items: Station)

    /** Delete every rows. */
    @Query("DELETE FROM stations") suspend fun clear()
}
