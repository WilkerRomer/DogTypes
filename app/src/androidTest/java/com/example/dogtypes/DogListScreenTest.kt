package com.example.dogtypes

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.dogtypes.core.api.ApiResponseStatus
import com.example.dogtypes.doglist.DogListScreen
import com.example.dogtypes.doglist.DogListViewModel
import com.example.dogtypes.doglist.DogTasks
import com.example.dogtypes.core.domain.Dog
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterialApi
class DogListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun progressBarShowsWhenLoadingState() {
        class FakeDogRepository: DogTasks {
            override suspend fun getDogCollection(): com.example.dogtypes.core.api.ApiResponseStatus<List<com.example.dogtypes.core.domain.Dog>> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Loading()
            }

            override suspend fun addDogToUser(dogId: Long): com.example.dogtypes.core.api.ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
           dogRepository =  FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { },
                onDogClicked = { },
                viewModel = viewModel,
            )
        }

        composeTestRule.onNodeWithTag(testTag = "loading-wheel").assertIsDisplayed()
    }

    @Test
    fun errorDialogShowsIfErrorGettingDogs() {
        class FakeDogRepository: DogTasks {
            override suspend fun getDogCollection(): com.example.dogtypes.core.api.ApiResponseStatus<List<com.example.dogtypes.core.domain.Dog>> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Error(R.string.there_was_an_error)
            }

            override suspend fun addDogToUser(dogId: Long): com.example.dogtypes.core.api.ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): com.example.dogtypes.core.api.ApiResponseStatus<com.example.dogtypes.core.domain.Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository =  FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { },
                onDogClicked = { },
                viewModel = viewModel,
            )
        }

        composeTestRule.onNodeWithTag(testTag = "error-dialog").assertIsDisplayed()
    }

    @Test
    fun dogListShowsIfSuccessGettingDogs() {
        val dog1Name = "Chihuahua"
        val dog2Name = "Guillermo"
        class FakeDogRepository: DogTasks {
            override suspend fun getDogCollection(): com.example.dogtypes.core.api.ApiResponseStatus<List<com.example.dogtypes.core.domain.Dog>> {
                return com.example.dogtypes.core.api.ApiResponseStatus.Success(
                    listOf(
                        com.example.dogtypes.core.domain.Dog(
                            1, 1, dog1Name, "", "", "",
                            "", "", "", "", "",
                            inCollection = true
                        ),
                        com.example.dogtypes.core.domain.Dog(
                            19, 23, dog2Name, "", "", "",
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
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository =  FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onNavigationIconClick = { },
                onDogClicked = { },
                viewModel = viewModel,
            )
        }

        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "dog-${dog1Name}").assertIsDisplayed()
        composeTestRule.onNodeWithText("23").assertIsDisplayed()

    }
}