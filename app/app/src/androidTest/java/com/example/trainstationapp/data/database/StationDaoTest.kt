package com.example.trainstationapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trainstationapp.utils.AndroidTestUtils
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
internal class StationDaoTest {
    private lateinit var dao: StationDao
    private lateinit var db: Database

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
        dao = db.stationDao()
    }

    @Test
    fun insertAndWatch() = runBlocking {
        val items =
            listOf(
                AndroidTestUtils.createStationModel("0"),
                AndroidTestUtils.createStationModel("1")
            )

        // Act
        val resultDeferred = async { dao.watch().first() }
        dao.insert(items)

        // Assert
        val result = resultDeferred.await()
        assertEquals(items, result)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }
}
