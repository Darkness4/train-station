package com.example.trainstationapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trainstationapp.data.models.StationModel
import kotlinx.coroutines.flow.Flow

/** Cache for storing the `StationModel`. */
@Dao
interface StationDao {
    /** Observe the whole cache */
    @Query("SELECT * FROM stations") fun watch(): Flow<List<StationModel>>

    /** Observe one item from the cache */
    @Query("SELECT * FROM stations WHERE id = :id") fun watchById(id: String): Flow<StationModel>

    /** Fetch a paging source from cache, with a search filters. */
    @Query("SELECT * FROM stations WHERE libelle LIKE '%' || :search || '%' ORDER BY libelle, id")
    fun watchAsPagingSource(search: String): PagingSource<Int, StationModel>

    /** Fetch a paging source from cache. */
    @Query("SELECT * FROM stations ORDER BY libelle, id")
    fun watchAsPagingSource(): PagingSource<Int, StationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(items: List<StationModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(vararg items: StationModel)

    /** Delete every rows. */
    @Query("DELETE FROM stations") suspend fun clear()
}
