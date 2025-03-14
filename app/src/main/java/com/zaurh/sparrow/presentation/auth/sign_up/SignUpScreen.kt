package com.zaurh.sparrow.presentation.auth.sign_up

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.navigation.Screen
import com.zaurh.sparrow.presentation.auth.sign_up.components.SignUpContent
import com.zaurh.sparrow.presentation.auth.sign_up.components.SignUpTopBar
import com.zaurh.sparrow.shared.AuthViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { SignUpTopBar(onBackClick = { navController.popBackStack() }) },
        content = { padding ->
            SignUpContent(
                modifier = modifier.padding(padding),
                authViewModel = authViewModel,
                signUpViewModel = signUpViewModel,
                onSuccess = {
                    navController.navigate(Screen.HomeScreen.route){
                        popUpTo(0)
                    }
                },
                onSignInClick = {
                    navController.popBackStack()
                }
            )
        }
    )

}