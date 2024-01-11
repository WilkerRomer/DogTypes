package com.example.dogtypes.doglist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.example.dogtypes.dogdetail.DogDetailComposeActivity
import com.example.dogtypes.dogdetail.ui.theme.DogTypesTheme
import com.example.dogtypes.domain.Dog
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalCoilApi
@AndroidEntryPoint
class DogListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogTypesTheme {
                DogListScreen(
                    onNavigationIconClick = ::onNavigationIconClick,
                    onDogClicked = ::openDogDetailActivity
                )
            }
        }
    }

    private fun openDogDetailActivity(dog: Dog){
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        startActivity(intent)
    }

    private fun onNavigationIconClick(){
        finish()
    }

}