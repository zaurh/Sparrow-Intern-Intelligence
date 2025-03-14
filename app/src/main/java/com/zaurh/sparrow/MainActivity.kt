package com.zaurh.sparrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.zaurh.sparrow.navigation.NavGraph
import com.zaurh.sparrow.navigation.Screen
import com.zaurh.sparrow.presentation.home.HomeScreen
import com.zaurh.sparrow.presentation.messages.MessagesScreen
import com.zaurh.sparrow.shared.AuthViewModel
import com.zaurh.sparrow.ui.theme.SparrowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel = hiltViewModel<AuthViewModel>()
            val signedIn = authViewModel.isSignedIn.collectAsState()

            SparrowTheme {
                NavGraph(
                    navController = navController,
                    startDestination =
                    if (signedIn.value) Screen.HomeScreen.route else Screen.SignInScreen.route,
                )
            }
        }
    }
}
