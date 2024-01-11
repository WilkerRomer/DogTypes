package com.example.dogtypes.viewmodel

import com.example.dogtypes.R
import com.example.dogtypes.core.api.ApiResponseStatus
import com.example.dogtypes.auth.AuthTasks
import com.example.dogtypes.auth.AuthViewModel
import com.example.dogtypes.core.domain.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var dogTypesCoroutineRule = DogTypesCoroutineRule()

    @Test
    fun testLoginValidationsCorrect() {
        class FakeAuthRepository: AuthTasks {
            override suspend fun login(email: String, password: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    com.example.dogtypes.core.domain.User(1, "peque@gmail.com", "")
                )
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    com.example.dogtypes.core.domain.User(1, "", "")
                )
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        viewModel.login("", "test1234")

        assertEquals(
            R.string.email_is_not_valid,
            viewModel.emailError.value)

        viewModel.login("peque@gmail.com", "")

        assertEquals(
            R.string.password_is_not_valid,
            viewModel.passwordError.value)
    }

    @Test
    fun testLoginStatesCorrect() {
        val fakeUser = com.example.dogtypes.core.domain.User(
            1, "peque@gmail.com",
            ""
        )
        class FakeAuthRepository : AuthTasks {
            override suspend fun login(email: String, password: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(fakeUser)
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    com.example.dogtypes.core.domain.User(1, "", "")
                )
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        viewModel.login("peque@gmail.com", "test1234")

        assertEquals(fakeUser.email, viewModel.user.value?.email )

    }
}