package com.example.dogtypes.doglist

import com.example.dogtypes.R
import com.example.dogtypes.api.ApiResponseStatus
import com.example.dogtypes.api.ApiService
import com.example.dogtypes.api.dto.AddDogToUserDTO
import com.example.dogtypes.api.dto.DogDTOMapper
import com.example.dogtypes.api.makeNetworkCall
import com.example.dogtypes.domain.Dog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DogTasks {

    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>>
    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any>
    suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog>
    suspend fun getProbableDogs(probableDogsIds: ArrayList<String>): Flow<ApiResponseStatus<Dog>>
}

class DogRepository @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher,
) : DogTasks {

    override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
        return withContext(dispatcher){
            val allDogsListDeferred = async {downloadDogs()}
            val userDogsListDeferred = async {getUserListDogs()}

            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse  = userDogsListDeferred.await()

            if (allDogsListResponse is ApiResponseStatus.Error){
                allDogsListResponse
            }else if (userDogsListResponse is ApiResponseStatus.Error){
                userDogsListResponse
            }else if (allDogsListResponse is ApiResponseStatus.Success &&
                userDogsListResponse is ApiResponseStatus.Success
            ) {
                ApiResponseStatus.Success(
                    getCollectionList(
                        allDogsListResponse.data,
                        userDogsListResponse.data
                    )
                )
            }else {
                ApiResponseStatus.Error(R.string.unknown_error)
            }
        }
    }

    private fun getCollectionList(allDogsList: List<Dog>, userDogsList: List<Dog>) =
        allDogsList.map {
            if (userDogsList.contains(it)){
                it
            } else {
                Dog(
                    0, it.index, "", "", "", "",
                    "", "", "", "", "",
                    inCollection = false
                )
            }
        }.sorted()

    private suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> =
        makeNetworkCall {
            val dogListApiResponse = apiService.getAllDogs()
            val dogDTOList = dogListApiResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }

    private suspend fun getUserListDogs(): ApiResponseStatus<List<Dog>> =
        makeNetworkCall {
            val dogListApiResponse = apiService.getUserListDogs()
            val dogDTOList = dogListApiResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }


    override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> =
        makeNetworkCall {
            val addDogToUserDTO = AddDogToUserDTO(dogId)
            val defaultResponse = apiService.addDogToUser(addDogToUserDTO)

            if (!defaultResponse.isSuccess) {
                throw Exception(defaultResponse.message)
            }
        }

    override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> =
        makeNetworkCall {
            val response = apiService.getDogByMlId(mlDogId)

            if (!response.isSuccess) {
                throw Exception(response.message)
            }

            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOToDogDomain(response.data.dog)
        }

    override suspend fun getProbableDogs(probableDogsIds: ArrayList<String>): Flow<ApiResponseStatus<Dog>> =
        flow{
        for (mlDogId in probableDogsIds) {
            val dog = getDogByMlId(mlDogId)
            emit(dog)
        }
    }.flowOn(dispatcher)
}



