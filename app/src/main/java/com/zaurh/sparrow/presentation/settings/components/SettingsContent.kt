package com.zaurh.sparrow.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.zaurh.sparrow.navigation.Screen
import com.zaurh.sparrow.shared.AuthViewModel

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Column(
        modifier.fillMaxSize()
    ) {
        SettingsItem(
            title = "Account",
            icon = Icons.Default.Person,
            onClick = {
                navController.navigate(Screen.EditAccountScreen.route)
            }
        )

        SettingsItem(
            title = "Notifications",
            icon = Icons.Default.Notifications,
            onClick = {

            }
        )
        SettingsItem(
            title = "Likes",
            icon = Icons.Default.Favorite,
            onClick = {

            }
        )
        SettingsItem(
            color = MaterialTheme.colorScheme.error,
            title = "Sign out",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = {
                authViewModel.logout()
            }
        )
    }

}