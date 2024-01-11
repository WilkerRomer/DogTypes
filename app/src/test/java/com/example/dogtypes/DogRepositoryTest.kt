package com.example.dogtypes

import com.example.dogtypes.core.api.ApiResponseStatus
import com.example.dogtypes.core.api.ApiService
import com.example.dogtypes.core.api.dto.AddDogToUserDTO
import com.example.dogtypes.core.api.dto.DogDTO
import com.example.dogtypes.core.api.dto.LoginDTO
import com.example.dogtypes.core.api.dto.SignUpDTO
import com.example.dogtypes.core.api.responses.AuthApiResponse
import com.example.dogtypes.core.api.responses.DefaultResponse
import com.example.dogtypes.core.api.responses.DogApiResponse
import com.example.dogtypes.core.api.responses.DogListApiResponse
import com.example.dogtypes.core.api.responses.DogResponse
import com.example.dogtypes.core.api.responses.DogsListResponse
import com.example.dogtypes.doglist.DogRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.UnknownHostException

class DogRepositoryTest {

    @Test
    fun testGetDogCollectionSuccess(): Unit = runBlocking {
        class FakeApiService: com.example.dogtypes.core.api.ApiService {
            override suspend fun getAllDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                return com.example.dogtypes.core.api.responses.DogListApiResponse(
                    message = "",
                    isSuccess = true,
                    data = com.example.dogtypes.core.api.responses.DogsListResponse(
                        dogs = listOf(
                            com.example.dogtypes.core.api.dto.DogDTO(
                                1, 1, "Shiwawa", "", "", "",
                                "", "", "", "", "",

                                ),
                            com.example.dogtypes.core.api.dto.DogDTO(
                                19, 2, "Pug", "", "", "",
                                "", "", "", "", "",
                            ),
                        )
                    )
                )
            }

            override suspend fun signUp(signUpDTO: com.example.dogtypes.core.api.dto.SignUpDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: com.example.dogtypes.core.api.dto.LoginDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: com.example.dogtypes.core.api.dto.AddDogToUserDTO): com.example.dogtypes.core.api.responses.DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserListDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                return com.example.dogtypes.core.api.responses.DogListApiResponse(
                    message = "",
                    isSuccess = true,
                    data = com.example.dogtypes.core.api.responses.DogsListResponse(
                        dogs = listOf(
                            com.example.dogtypes.core.api.dto.DogDTO(
                                19, 2, "Pug", "", "", "",
                                "", "", "", "", "",
                            ),
                        )
                    )
                )
            }

            override suspend fun getDogByMlId(mlId: String): com.example.dogtypes.core.api.responses.DogApiResponse {
                TODO("Not yet implemented")
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogCollection()
        assert(apiResponseStatus is com.example.dogtypes.core.api.ApiResponseStatus.Success)
        val dogCollection = (apiResponseStatus as com.example.dogtypes.core.api.ApiResponseStatus.Success).data
        assertEquals(2, dogCollection.size)
        assertEquals("Pug", dogCollection[1].name)
        assertEquals("", dogCollection[0].name)
    }

    @Test
    fun testGetAllDogsError(): Unit = runBlocking {
        class FakeApiService: com.example.dogtypes.core.api.ApiService {
            override suspend fun getAllDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                throw UnknownHostException()
            }

            override suspend fun signUp(signUpDTO: com.example.dogtypes.core.api.dto.SignUpDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: com.example.dogtypes.core.api.dto.LoginDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: com.example.dogtypes.core.api.dto.AddDogToUserDTO): com.example.dogtypes.core.api.responses.DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserListDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                return com.example.dogtypes.core.api.responses.DogListApiResponse(
                    message = "",
                    isSuccess = true,
                    data = com.example.dogtypes.core.api.responses.DogsListResponse(
                        dogs = listOf(
                            com.example.dogtypes.core.api.dto.DogDTO(
                                19, 2, "Pug", "", "", "",
                                "", "", "", "", "",
                            ),
                        )
                    )
                )
            }

            override suspend fun getDogByMlId(mlId: String): com.example.dogtypes.core.api.responses.DogApiResponse {
                TODO("Not yet implemented")
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogCollection()
        assert(apiResponseStatus is com.example.dogtypes.core.api.ApiResponseStatus.Error)
        assertEquals(R.string.unknown_host_exception,
            (apiResponseStatus as com.example.dogtypes.core.api.ApiResponseStatus.Error).messageId)
    }

    @Test
    fun getDogByMlSuccess() = runBlocking {
        val resultId = 19L
        class FakeApiService : com.example.dogtypes.core.api.ApiService {
            override suspend fun getAllDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(signUpDTO: com.example.dogtypes.core.api.dto.SignUpDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: com.example.dogtypes.core.api.dto.LoginDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: com.example.dogtypes.core.api.dto.AddDogToUserDTO): com.example.dogtypes.core.api.responses.DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserListDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlId: String): com.example.dogtypes.core.api.responses.DogApiResponse {
                return com.example.dogtypes.core.api.responses.DogApiResponse(
                    message = "",
                    isSuccess = true,
                    data = com.example.dogtypes.core.api.responses.DogResponse(
                        dog = com.example.dogtypes.core.api.dto.DogDTO(
                            resultId, 2, "Pug", "", "", "",
                            "", "", "", "", "",
                        )
                    )
                )
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogByMlId("kjfdjvkhbdjhb")
        assert(apiResponseStatus is com.example.dogtypes.core.api.ApiResponseStatus.Success)
        assertEquals(resultId, (apiResponseStatus as com.example.dogtypes.core.api.ApiResponseStatus.Success).data.id)
    }

    @Test
    fun getDogByMlError() = runBlocking {
        val resultId = 19L
        class FakeApiService : com.example.dogtypes.core.api.ApiService {
            override suspend fun getAllDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(signUpDTO: com.example.dogtypes.core.api.dto.SignUpDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: com.example.dogtypes.core.api.dto.LoginDTO): com.example.dogtypes.core.api.responses.AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: com.example.dogtypes.core.api.dto.AddDogToUserDTO): com.example.dogtypes.core.api.responses.DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserListDogs(): com.example.dogtypes.core.api.responses.DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlId: String): com.example.dogtypes.core.api.responses.DogApiResponse {
                return com.example.dogtypes.core.api.responses.DogApiResponse(
                    message = "error_getting_dog_by_ml_id",
                    isSuccess = false,
                    data = com.example.dogtypes.core.api.responses.DogResponse(
                        dog = com.example.dogtypes.core.api.dto.DogDTO(
                            resultId, 2, "Pug", "", "", "",
                            "", "", "", "", "",
                        )
                    )
                )
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogByMlId("kjfdjvkhbdjhb")
        assert(apiResponseStatus is com.example.dogtypes.core.api.ApiResponseStatus.Error)
        assertEquals(R.string.unknown_error, (apiResponseStatus as com.example.dogtypes.core.api.ApiResponseStatus.Error).messageId)
    }
}