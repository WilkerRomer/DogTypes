package com.example.dogtypes.api

import com.example.dogtypes.ADD_DOG_TO_USER_URL
import com.example.dogtypes.GET_ALL_DOGS_URL
import com.example.dogtypes.GET_DOG_BY_ML_ID
import com.example.dogtypes.GET_USER_DOGS_URL
import com.example.dogtypes.SIGN_IN_URL
import com.example.dogtypes.SIGN_UP_URL
import com.example.dogtypes.api.dto.AddDogToUserDTO
import com.example.dogtypes.api.dto.LoginDTO
import com.example.dogtypes.api.dto.SignUpDTO
import com.example.dogtypes.api.responses.AuthApiResponse
import com.example.dogtypes.api.responses.DefaultResponse
import com.example.dogtypes.api.responses.DogApiResponse
import com.example.dogtypes.api.responses.DogListApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET(GET_ALL_DOGS_URL)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @POST(SIGN_IN_URL)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @Headers( "${ApiServiceInterceptor.NEED_AUTH_HEADER_KEY}: true")
    @POST(ADD_DOG_TO_USER_URL)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO): DefaultResponse

    @Headers( "${ApiServiceInterceptor.NEED_AUTH_HEADER_KEY}: true")
    @GET(GET_USER_DOGS_URL)
    suspend fun getUserListDogs(): DogListApiResponse

    @GET(GET_DOG_BY_ML_ID)
    suspend fun getDogByMlId(@Query("ml_id") mlId: String): DogApiResponse
}
