package com.example.dogtypes.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.example.dogtypes.dogdetail.ui.theme.DogTypesTheme
import com.example.dogtypes.domain.User
import com.example.dogtypes.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DogTypesTheme {
                AuthScreen(
                    onUserLoggedInt = ::startMainActivity,
                )
            }
        }
        /*val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.status.observe(this){

           status ->

            when(status){
                is ApiResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    showErrorDialog(status.messageId)
                }
                is ApiResponseStatus.Loading ->  binding.loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> binding.loadingWheel.visibility = View.GONE
            }
        }

        viewModel.user.observe(this){
            user ->
            if (user != null){
                User.setLoggedInUser(this, user)
                startMainActivity()
            }
        }*/
    }


    private fun startMainActivity(userValue: User) {
        User.setLoggedInUser(this, userValue)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}