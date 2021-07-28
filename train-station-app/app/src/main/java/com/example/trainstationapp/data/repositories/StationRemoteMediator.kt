package com.example.trainstationapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.datasources.TrainStationDataSource
import com.example.trainstationapp.data.models.RemoteKeys
import com.example.trainstationapp.data.models.StationModel
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import retrofit2.HttpException
import java.io.IOException

/**
 * This `StationRemoteMediator` handles paging from a layered data source.
 *
 * @see [Paging 3 library
 * overview](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
 */
@ExperimentalPagingApi
class StationRemoteMediator(
    private val search: String,
    private val service: TrainStationDataSource,
    private val database: Database,
    private val token: String
) : RemoteMediator<Int, StationModel>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    /**
     * Load data to the database on page actions.
     *
     * Steps :
     * 1. Find out what page we need to load from the network, based on the LoadType.
     * 2. Trigger the network request.
     * 3. Once the network request completes, if the received list is not empty,
     * ```
     *     then do the following:
     * ```
     * 4. We compute the RemoteKeys for every item.
     * 5. If this is a new query (loadType = REFRESH) then we clear the database.
     * 6. Save the RemoteKeys and items in the database.
     * 7. Return MediatorResult.Success(endOfPaginationReached = false).
     * 8. If the list of repos was empty then we return MediatorResult.Success(endOfPaginationReached
     * = true).
     * ```
     *     If we get an error requesting data we return MediatorResult.Error.
     * ```
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StationModel>
    ): MediatorResult {
        val page =
            when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

        return try {
            val response =
                if (search.isNotEmpty()) {
                    val apiQuery =
                        buildJsonObject { putJsonObject("libelle") { put("\$cont", search) } }
                    service.find(
                        s = apiQuery.toString(),
                        page = page,
                        limit = state.config.pageSize,
                        token = token
                    )
                } else {
                    service.find(page = page, limit = state.config.pageSize, token = token)
                }

            val items = response.data
            val endOfPaginationReached = items.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clear()
                    database.stationDao().clear()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys =
                    items.map { RemoteKeys(id = it.recordid, prevKey = prevKey, nextKey = nextKey) }
                database.remoteKeysDao().insert(keys)
                database.stationDao().insert(items)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, StationModel>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.recordid?.let { recordid ->
                database.remoteKeysDao().findById(recordid)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StationModel>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { item ->
            // Get the remote keys of the first items retrieved
            database.remoteKeysDao().findById(item.recordid)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StationModel>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { item ->
            // Get the remote keys of the last item retrieved
            database.remoteKeysDao().findById(item.recordid)
        }
    }
}
