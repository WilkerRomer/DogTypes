package com.example.dogtypes

import android.Manifest.permission.CAMERA
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import coil.annotation.ExperimentalCoilApi
import com.example.dogtypes.core.api.ApiResponseStatus
import com.example.dogtypes.auth.AuthTasks
import com.example.dogtypes.auth.LoginActivity
import com.example.dogtypes.di.AuthTasksModule
import com.example.dogtypes.core.domain.User
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoilApi
@ExperimentalMaterialApi
@UninstallModules(AuthTasksModule::class)
@HiltAndroidTest
class LoginActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @get:Rule(order = 2)
    val runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(CAMERA)

    class FakeAuthRepository @Inject constructor(): AuthTasks {
        override suspend fun login(email: String, password: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
            return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                com.example.dogtypes.core.domain.User(1L, "peque@gmail.com", "kmkhvrsyhvbhb")
            )
        }

        override suspend fun signUp(
            email: String,
            password: String,
            passwordConfirmation: String
        ): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.User> {
            TODO("Not yet implemented")
        }

    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class AuthTasksTestModule {

        @Binds
        abstract fun bindDogTasks(
            fakeAuthRepository: FakeAuthRepository
        ): AuthTasks
    }

    @Test
    fun mainActivityOpensAfterUserLogin() {
        val context = composeTestRule.activity

        composeTestRule
            .onNodeWithText(context.getString(R.string.login))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(useUnmergedTree = true, testTag = "email-field")
            .performTextInput("peque@gmail.com")

        composeTestRule
            .onNodeWithTag(useUnmergedTree = true, testTag = "password-field")
            .performTextInput("05012020")

        composeTestRule
            .onNodeWithText(context.getString(R.string.login))
            .performClick()

        onView(withId(R.id.take_photo_fab)).check(matches(isDisplayed()))

    }
}