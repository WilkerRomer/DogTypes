package com.example.dogtypes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.dogtypes.core.api.ApiResponseStatus
import com.example.dogtypes.auth.AuthScreen
import com.example.dogtypes.auth.AuthTasks
import com.example.dogtypes.auth.AuthViewModel
import com.example.dogtypes.core.domain.User
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testTappingRegisterButtonOpenSignUpScreen() {
        class FakeAuthRepository: AuthTasks {
            override suspend fun login(email: String, password: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        composeTestRule.setContent {
            AuthScreen(
                onUserLoggedInt = { },
                authViewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(testTag = "login-button").assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTag = "login-screen-register-button").performClick()
        composeTestRule.onNodeWithTag(testTag = "sign-up-button").assertIsDisplayed()
    }

    @Test
    fun testEmailErrorShowsIfTappingLoginButtonAndNotEmail() {
        class FakeAuthRepository: AuthTasks {
            override suspend fun login(email: String, password: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        composeTestRule.setContent {
            AuthScreen(
                onUserLoggedInt = { },
                authViewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(testTag = "login-button").performClick()
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "email-field-error").assertIsDisplayed()
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "email-field").performTextInput("peque@gmail.com")
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "login-button").performClick()
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "password-field-error").assertIsDisplayed()
    }
}