package com.example.trainstationapp.data.repositories

import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.database.RemoteKeysDao
import com.example.trainstationapp.data.database.StationDao
import com.example.trainstationapp.data.datasources.TrainStationDataSource
import com.example.trainstationapp.data.models.MakeFavoriteModel
import com.example.trainstationapp.utils.TestUtils
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.Called
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

class StationRepositoryImplTest :
    WordSpec({
        val remote = mockk<TrainStationDataSource>()
        val local = mockk<Database>()
        val stationDao = mockk<StationDao>()
        val remoteKeysDao = mockk<RemoteKeysDao>()
        val repository = StationRepositoryImpl(remote, local)

        beforeTest {
            clearAllMocks()
            every { local.stationDao() } returns stationDao
            every { local.remoteKeysDao() } returns remoteKeysDao
        }

        "createOne" should
            {
                "create" {
                    // Arrange
                    val slot = slot<StationModel>()
                    coEvery { remote.create(capture(slot), any()) } coAnswers { slot.captured }
                    coEvery { stationDao.insert(any<StationModel>()) } just Runs
                    val station = TestUtils.createStation("0")

                    // Act
                    val result = repository.createOne(station, "token")

                    // Assert
                    coVerify { remote.create(station.asModel(), "Bearer token") }
                    coVerify { stationDao.insert(station.asModel()) }
                    result.isSuccess.shouldBeTrue()
                }

                "return Failure on throw" {
                    // Arrange
                    val error = Exception("An Error")
                    coEvery { remote.create(any(), any()) } throws error
                    val station = TestUtils.createStation("0")

                    // Act
                    val result = repository.createOne(station, "token")

                    // Assert
                    verify { stationDao wasNot Called }
                    result.isFailure.shouldBeTrue()
                }
            }

        "makeFavoriteOne" should
            {
                "make one favorite" {
                    // Arrange
                    val slot = slot<MakeFavoriteModel>()
                    val mock = TestUtils.createStation("0").asModel()
                    coEvery { remote.makeFavoriteById(any(), capture(slot), any()) } coAnswers
                        {
                            mock
                        }
                    coEvery { stationDao.insert(any<StationModel>()) } just Runs
                    val station = TestUtils.createStation("0")

                    // Act
                    val result =
                        repository.makeFavoriteOne(station.id, !station.isFavorite, "token")

                    // Assert
                    coVerify {
                        remote.makeFavoriteById(
                            station.id,
                            MakeFavoriteModel(!station.isFavorite),
                            "Bearer token"
                        )
                    }
                    coVerify { stationDao.insert(mock) }
                    result.isSuccess.shouldBeTrue()
                }

                "return Failure on null" {
                    // Arrange
                    coEvery { remote.makeFavoriteById(any(), any(), any()) } returns null
                    val station = TestUtils.createStation("0")

                    // Act
                    val result = repository.makeFavoriteOne(station.id, station.isFavorite, "token")

                    // Assert
                    coVerify {
                        remote.makeFavoriteById(
                            station.id,
                            MakeFavoriteModel(station.isFavorite),
                            "Bearer token"
                        )
                    }
                    verify { stationDao wasNot Called }
                    result.isFailure.shouldBeTrue()
                }

                "return Failure on throw" {
                    // Arrange
                    val error = Exception("An Error")
                    coEvery { remote.makeFavoriteById(any(), any(), any()) } throws error
                    val station = TestUtils.createStation("0")

                    // Act
                    val result = repository.makeFavoriteOne(station.id, station.isFavorite, "token")

                    // Assert
                    verify { stationDao wasNot Called }
                    result.isFailure.shouldBeTrue()
                }
            }
    })
