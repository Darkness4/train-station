package com.example.trainstationapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trainstationapp.data.models.RemoteKeys

/**
 * This DAO permits to save the *index* of the page of an object.
 *
 * Because when we get the last item loaded from the `PagingState` there's no way to know the index
 * of the page it belonged to. To solve this problem, we can add another table that stores the next
 * and previous page keys for each object; we can call it `remote_keys`. While this can be done in
 * the Repo table, creating a new table for the next and previous remote keys associated with a Repo
 * allows us to have a better **separation of concerns**.
 */
@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun findById(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clear()
}
