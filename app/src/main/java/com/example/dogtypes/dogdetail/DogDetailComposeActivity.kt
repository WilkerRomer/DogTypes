package com.example.dogtypes.dogdetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import coil.annotation.ExperimentalCoilApi
import com.example.dogtypes.dogdetail.ui.theme.DogTypesTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@AndroidEntryPoint
class DogDetailComposeActivity : ComponentActivity() {

    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
        const val MOST_PROBABLE_DOGS_IDS = "most_probable_dogs_ids"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                DogTypesTheme {
                    DogDetailScreen(
                        finishActivity = { finish() }
                    )
              }
        }
    }
}
