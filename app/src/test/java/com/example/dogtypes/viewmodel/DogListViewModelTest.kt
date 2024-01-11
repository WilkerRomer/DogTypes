package com.example.dogtypes.viewmodel

import com.example.dogtypes.core.api.ApiResponseStatus
import com.example.dogtypes.doglist.DogListViewModel
import com.example.dogtypes.doglist.DogTasks
import com.example.dogtypes.core.domain.Dog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DogListViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var dogTypesCoroutineRule = DogTypesCoroutineRule()

    @Test
    fun downloadDogListStatusesCorrect() {
        class FakeDogRepository: DogTasks {
            override suspend fun getDogCollection(): com.example.dogtypes.core.api.ApiResponseStatus<List<com.example.dogtypes.core.domain.Dog>> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    listOf(
                        com.example.dogtypes.core.domain.Dog(
                            1, 1, "", "", "", "",
                            "", "", "", "", "",
                            inCollection = false
                        ),
                        com.example.dogtypes.core.domain.Dog(
                            19, 2, "", "", "", "",
                            "", "", "", "", "",
                            inCollection = false
                        ),
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): com.example.dogtypes.core.api.ApiResponseStatus<Any> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.Dog> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    com.example.dogtypes.core.domain.Dog(
                        1, 1, "", "", "", "",
                        "", "", "", "", "",
                        inCollection = false
                    )
                )
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(2, viewModel.dogList.value.size)
        assertEquals(19, viewModel.dogList.value[1].id)
        assert(viewModel.status.value is com.example.dogtypes.core.api.ApiResponseStatus.Success )

    }

    @Test
    fun downloadDogListErrorStatusesCorrect() {
        class FakeDogRepository: DogTasks {
            override suspend fun getDogCollection(): com.example.dogtypes.core.api.ApiResponseStatus<List<com.example.dogtypes.core.domain.Dog>> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Error(messageId = 12)
            }

            override suspend fun addDogToUser(dogId: Long): com.example.dogtypes.core.api.ApiResponseStatus<Any> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.Dog> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    com.example.dogtypes.core.domain.Dog(
                        1, 1, "", "", "", "",
                        "", "", "", "", "",
                        inCollection = false
                    )
                )
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(0, viewModel.dogList.value.size)
        assert(viewModel.status.value is com.example.dogtypes.core.api.ApiResponseStatus.Error )

    }

    @Test
    fun resetStatusCorrect() {
        class FakeDogRepository: DogTasks {
            override suspend fun getDogCollection(): com.example.dogtypes.core.api.ApiResponseStatus<List<com.example.dogtypes.core.domain.Dog>> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Error(messageId = 12)
            }

            override suspend fun addDogToUser(dogId: Long): com.example.dogtypes.core.api.ApiResponseStatus<Any> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.Dog> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    com.example.dogtypes.core.domain.Dog(
                        1, 1, "", "", "", "",
                        "", "", "", "", "",
                        inCollection = false
                    )
                )
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assert(viewModel.status.value is com.example.dogtypes.core.api.ApiResponseStatus.Error)

        viewModel.resetApiResponseStatus()

        assert(viewModel.status.value == null)
    }
}