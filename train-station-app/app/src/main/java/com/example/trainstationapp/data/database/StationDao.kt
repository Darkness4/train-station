package com.example.trainstationapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trainstationapp.data.models.StationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Query("SELECT * FROM stations")
    fun watch(): Flow<List<StationModel>>

    @Query("SELECT * FROM stations")
    fun watchAsPagingSource(): PagingSource<Int, StationModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<StationModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg items: StationModel)

    @Query("DELETE FROM stations")
    suspend fun clear()
}
