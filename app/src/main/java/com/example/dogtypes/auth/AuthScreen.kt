package com.example.dogtypes.auth

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dogtypes.api.ApiResponseStatus
import com.example.dogtypes.auth.AuthNavDestinations.LoginScreenDestination
import com.example.dogtypes.auth.AuthNavDestinations.SignUpScreenDestination
import com.example.dogtypes.composables.ErrorDialog
import com.example.dogtypes.composables.LoadingWheel
import com.example.dogtypes.domain.User

@Composable
fun AuthScreen(
    onUserLoggedInt: (User) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val user = authViewModel.user

    val userValue = user.value
    if (userValue != null) {
        onUserLoggedInt(userValue)
    }


    val navController = rememberNavController()
    val status = authViewModel.status.value

    AuthNavHost(
        navController = navController,
        onLoginButtonClick = {email, password -> authViewModel.login(email, password)},
        onSignUpButtonClick = {email, password, confirmPassword ->
            authViewModel.signUp(email, password, confirmPassword)},
        authViewModel = authViewModel,
    )

    if (status is ApiResponseStatus.Loading) {
        LoadingWheel()
    } else if (status is ApiResponseStatus.Error){
        ErrorDialog(status.messageId) { authViewModel.resetApiResponseStatus() }
    }
}
@Composable
fun AuthNavHost(
    navController: NavHostController,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (email: String, password: String, confirmPassword: String) -> Unit,
    authViewModel: AuthViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = LoginScreenDestination
    ) {
        composable(route = LoginScreenDestination) {
            LoginScreen(
                onLoginButtonClick = onLoginButtonClick,
                onRegisterButtonClick = {
                    navController.navigate(route = SignUpScreenDestination)
                },
                authViewModel = authViewModel,
            )
        }

        composable(route = SignUpScreenDestination) {
            SingUpScreen(
                onSignUpButtonClick = onSignUpButtonClick,
                onNavigationIconClick = { navController.navigateUp()
                },
                authViewModel = authViewModel,
            )
        }
    }
}