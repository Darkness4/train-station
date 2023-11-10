package com.example.trainstationapp.data.repositories

import androidx.datastore.core.DataStore
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.database.RemoteKeysDao
import com.example.trainstationapp.data.database.StationDao
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.datastore.jwt
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIGrpcKt
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.getOneStationRequest
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.getOneStationResponse
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.setFavoriteOneStationRequest
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.setFavoriteOneStationResponse
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.utils.TestUtils
import io.grpc.Status
import io.grpc.StatusException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
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
import kotlinx.coroutines.flow.flow

class StationRepositoryImplTest :
    WordSpec({
        val remote = mockk<StationAPIGrpcKt.StationAPICoroutineStub>()
        val local = mockk<Database>()
        val stationDao = mockk<StationDao>()
        val remoteKeysDao = mockk<RemoteKeysDao>()
        val auth = mockk<DataStore<Session.Jwt>>()
        val repository = StationRepositoryImpl(remote, local, auth)

        beforeTest {
            clearAllMocks()
            every { local.stationDao() } returns stationDao
            every { local.remoteKeysDao() } returns remoteKeysDao
        }

        "makeFavoriteOne" should
            {
                "make one favorite" {
                    // Arrange
                    val slot = slot<StationProto.SetFavoriteOneStationRequest>()
                    coEvery { remote.setFavoriteOneStation(capture(slot), any()) } coAnswers
                        {
                            setFavoriteOneStationResponse {}
                        }
                    coEvery { stationDao.insert(any<Station>()) } just Runs
                    val station = TestUtils.createStation("0")
                    coEvery { remote.getOneStation(any(), any()) } coAnswers
                        {
                            getOneStationResponse {
                                this.station =
                                    station.copy(isFavorite = !station.isFavorite).asGrpcModel()
                            }
                        }
                    coEvery { auth.data } coAnswers { flow { emit(jwt { token = "token" }) } }

                    // Act
                    repository.makeFavoriteOne(station.id, !station.isFavorite)

                    // Assert
                    coVerify {
                        remote.setFavoriteOneStation(
                            setFavoriteOneStationRequest {
                                id = station.id
                                token = "token"
                                value = !station.isFavorite
                            },
                            any()
                        )
                    }
                    coVerify {
                        remote.getOneStation(
                            getOneStationRequest {
                                id = station.id
                                token = "token"
                            },
                            any()
                        )
                    }
                    coVerify { stationDao.insert(station.copy(isFavorite = !station.isFavorite)) }
                }

                "throw" {
                    // Arrange
                    coEvery { remote.setFavoriteOneStation(any(), any()) } throws
                        StatusException(Status.NOT_FOUND)
                    coEvery { auth.data } coAnswers { flow { emit(jwt { token = "token" }) } }
                    val station = TestUtils.createStation("0")

                    // Act
                    shouldThrow<StatusException> {
                        repository.makeFavoriteOne(station.id, station.isFavorite)
                    }

                    // Assert
                    coVerify {
                        remote.setFavoriteOneStation(
                            setFavoriteOneStationRequest {
                                id = station.id
                                token = "token"
                                value = station.isFavorite
                            },
                            any()
                        )
                    }
                    verify { stationDao wasNot Called }
                }
            }
    })
