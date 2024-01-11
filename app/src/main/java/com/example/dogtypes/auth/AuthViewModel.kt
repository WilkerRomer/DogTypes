package com.example.dogtypes.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogtypes.R
import com.example.dogtypes.api.ApiResponseStatus
import com.example.dogtypes.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthTasks
): ViewModel() {

    var user = mutableStateOf<User?>(null)
        private set

    var emailError = mutableStateOf<Int?>(null)
        private set

    var passwordError =  mutableStateOf<Int?>(null)
        private set

    var confirmPasswordError =  mutableStateOf<Int?>(null)
        private set

    var status = mutableStateOf<ApiResponseStatus<User>?>(null)
        private set


    fun login(email: String, password: String) {
        when {
            email.isEmpty() -> emailError.value = R.string.email_is_not_valid
            password.isEmpty() -> passwordError.value = R.string.password_is_not_valid
            else -> {
                viewModelScope.launch {
                    status.value = ApiResponseStatus.Loading()
                    handleResponseStatus(
                        authRepository.login(
                            email,
                            password
                        )
                    )
                }
            }
        }
    }

    fun signUp(email: String, password: String,
               passwordConfirmation: String
    ) {
        when {
            email.isEmpty() -> {
                emailError.value = R.string.email_is_not_valid
            }
            password.isEmpty() -> {
                passwordError.value = R.string.password_is_not_valid
            }
            passwordConfirmation.isEmpty() -> {
                confirmPasswordError.value = R.string.password_is_not_valid
            }
            password != passwordConfirmation -> {
                passwordError.value = R.string.password_do_not_match
                confirmPasswordError.value = R.string.password_do_not_match
            }
            else -> {
                viewModelScope.launch {
                    status.value = ApiResponseStatus.Loading()
                    handleResponseStatus(
                        authRepository.signUp(
                            email, password,
                            passwordConfirmation
                        )
                    )
                }
            }
        }
    }

    fun resetError() {
        emailError.value = null
        passwordError.value = null
        confirmPasswordError.value = null
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<User>) {
        if (apiResponseStatus is ApiResponseStatus.Success){
            user.value = apiResponseStatus.data!!
        }
        status.value = apiResponseStatus
    }

    fun resetApiResponseStatus() {
        status.value = null
    }

}