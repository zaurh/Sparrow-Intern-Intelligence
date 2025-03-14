package com.zaurh.sparrow.presentation.auth.sign_in

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.zaurh.sparrow.navigation.Screen
import com.zaurh.sparrow.presentation.auth.sign_in.components.SignInContent
import com.zaurh.sparrow.presentation.auth.sign_in.components.SignInTopBar
import com.zaurh.sparrow.shared.AuthViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = { SignInTopBar() },
        content = { padding ->
            SignInContent(
                modifier = modifier.padding(padding),
                authViewModel = authViewModel,
                onSignUpClick = {
                    navController.navigate(Screen.SignUpScreen.route)
                },
                onSuccess = {
                    navController.navigate(Screen.HomeScreen.route){
                        popUpTo(0)
                    }
                }
            )
        }
    )
}