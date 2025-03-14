package com.zaurh.sparrow.presentation.account.user

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.presentation.account.user.components.UserContent
import com.zaurh.sparrow.shared.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    userId: String,
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val currentUserState = authViewModel.currentUserState.collectAsState()
    val userData = userViewModel.userData.collectAsState()

    LaunchedEffect(true) {
        userViewModel.getUserData(userId)
    }


    LaunchedEffect(userData.value.data?.userId) {
        userData.value.data?.userId?.let {
            userViewModel.getUserPosts(it)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    userData.value.data?.username?.let {
                        Text(it)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                }
            )
        },
        content = { padding ->
            UserContent(
                modifier = Modifier.padding(padding),
                navController = navController,
                userViewModel = userViewModel,
                userData = userData.value.data ?: UserData(),
                currentUser = currentUserState.value.data ?: UserData()
            )
        }
    )
}