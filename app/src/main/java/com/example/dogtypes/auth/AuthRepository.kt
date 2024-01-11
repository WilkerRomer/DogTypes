package com.example.dogtypes.auth

import com.example.dogtypes.api.ApiResponseStatus
import com.example.dogtypes.api.ApiService
import com.example.dogtypes.api.dto.LoginDTO
import com.example.dogtypes.api.dto.SignUpDTO
import com.example.dogtypes.api.dto.UserDTOMapper
import com.example.dogtypes.api.makeNetworkCall
import com.example.dogtypes.domain.User
import javax.inject.Inject

interface AuthTasks {
    suspend fun login(email: String, password: String): ApiResponseStatus<User>
    suspend fun signUp(email: String, password: String,
                       passwordConfirmation: String): ApiResponseStatus<User>
}

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
): AuthTasks{

    override suspend fun login(email: String, password: String): ApiResponseStatus<User> =
       makeNetworkCall {
            val loginDTO = LoginDTO(email, password)
            val loginResponse = apiService.login(loginDTO)

            if (!loginResponse.isSuccess) {
                throw Exception(loginResponse.message)
            }

            val userDTO = loginResponse.data.user
            val userDTOMapper = UserDTOMapper()
            userDTOMapper.fromUserDTOToUserDomain(userDTO)
        }

    override suspend fun signUp(email: String, password: String,
                                passwordConfirmation: String): ApiResponseStatus<User> =
        makeNetworkCall {
            val signUpDTO =
                SignUpDTO(email, password, passwordConfirmation)
            val signUpResponse = apiService.signUp(signUpDTO)

            if (!signUpResponse.isSuccess) {
                throw Exception(signUpResponse.message)
            }

            val userDTO = signUpResponse.data.user
            val userDTOMapper = UserDTOMapper()
            userDTOMapper.fromUserDTOToUserDomain(userDTO)
        }
}