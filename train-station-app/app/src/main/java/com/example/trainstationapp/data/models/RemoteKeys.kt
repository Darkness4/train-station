package com.example.trainstationapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * *RemoteKeys* remember the position of the `id` in the pages.
 *
 * Similarly to a linked list, there are pointers to the next and last page.
 *
 * @param id The ID of the object in a page.
 * @param prevKey Reference to the previous page. `null` means id is in the first page.
 * @param nextKey Reference to the next page. `null` means id is in the last page.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
