package com.example.dogtypes

import android.Manifest
import androidx.camera.core.ImageProxy
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import coil.annotation.ExperimentalCoilApi
import com.example.dogtypes.core.api.ApiResponseStatus
import com.example.dogtypes.di.ClassifierModule
import com.example.dogtypes.di.DogTasksModule
import com.example.dogtypes.doglist.DogTasks
import com.example.dogtypes.core.domain.Dog
import com.example.dogtypes.machinelearning.ClassifierTasks
import com.example.dogtypes.machinelearning.DogRecognition
import com.example.dogtypes.main.MainActivity
import com.example.dogtypes.testutils.EspressoIdlingResource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoilApi
@ExperimentalMaterialApi
@UninstallModules(DogTasksModule::class, ClassifierModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 2)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 3)
    val runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.CAMERA)

    class FakeDogRepository @Inject constructor(): DogTasks {
        override suspend fun getDogCollection(): com.example.dogtypes.core.api.ApiResponseStatus<List<com.example.dogtypes.core.domain.Dog>> {
            return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                listOf(
                    com.example.dogtypes.core.domain.Dog(
                        1, 1, "", "", "", "",
                        "", "", "", "", "",
                        inCollection = true
                    ),
                    com.example.dogtypes.core.domain.Dog(
                        19, 23, "", "", "", "",
                        "", "", "", "", "",
                        inCollection = false
                    ),
                )
            )
        }

        override suspend fun addDogToUser(dogId: Long): com.example.dogtypes.core.api.ApiResponseStatus<Any> {
            TODO("Not yet implemented")
        }

        override suspend fun getDogByMlId(mlDogId: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.Dog> {
            return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                com.example.dogtypes.core.domain.Dog(
                    89, 70, "Chow chow", "", "", "",
                    "", "", "", "", "",
                    inCollection = true
                )
            )
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DogTasksTestModule {

        @Binds
        abstract fun bindDogTasks(
            fakeDogRepository: FakeDogRepository
        ): DogTasks
    }

    class FakeClassifierRepository @Inject constructor(): ClassifierTasks{
        override suspend fun recognizeImage(imageProxy: ImageProxy): DogRecognition {
            return DogRecognition("jjhfbvegghty", 80.0f)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ClassifierTestModule {

        @Binds
        abstract fun bindClassifierTasks(
            fakeClassifierRepository: FakeClassifierRepository
        ): ClassifierTasks

    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun showAllFab() {
        onView(withId(R.id.take_photo_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.dog_list_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.settings_fab)).check(matches(isDisplayed()))
    }

    @Test
    fun dogListOpensWhenClickingButton() {
        onView(withId(R.id.dog_list_fab)).perform(click())

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val string = context.getString(R.string.my_dog_collection)
        composeTestRule.onNodeWithText(string).assertIsDisplayed()

    }

    @Test
    fun whenRecognizingDogDetailsScreenOpens() {
        onView(withId(R.id.take_photo_fab)).perform(click())
        composeTestRule.onNodeWithTag(testTag = "close-detail-screen-fab").assertIsDisplayed()
    }

}