package com.zaurh.sparrow.presentation.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.data.models.PostData
import com.zaurh.sparrow.presentation.account.profile.ProfileScreen
import com.zaurh.sparrow.presentation.account.profile.ProfileViewModel
import com.zaurh.sparrow.presentation.feed.FeedScreen
import com.zaurh.sparrow.presentation.home.components.TabItem
import com.zaurh.sparrow.presentation.home.components.tabs
import com.zaurh.sparrow.presentation.messages.MessagesScreen
import com.zaurh.sparrow.presentation.search.SearchScreen
import com.zaurh.sparrow.shared.AuthViewModel
import com.zaurh.sparrow.shared.uriToFile

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

    val currentUserData = authViewModel.currentUserState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null){
            profileViewModel.uploadImage(
                file = uriToFile(
                    context = navController.context,
                    uri = uri
                ),
                context = navController.context,
                onSuccess = { imageUri ->

                    currentUserData.value.data?.userId?.let { userId ->
                        profileViewModel.addPost(
                            postData = PostData(
                                authorId = userId,
                                image = imageUri
                            )
                        )
                    }
                }
            )

        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        content = { padding ->
            HorizontalPager(
                state = pagerState,
                modifier = modifier.padding(padding),
                pageContent = { index ->
                    when (index) {
                        0 -> FeedScreen(
                            navController = navController,
                            authViewModel = authViewModel
                        )
                        1 -> SearchScreen(
                            navController = navController
                        )
                        2 -> MessagesScreen(
                            authViewModel = authViewModel,
                            navController = navController
                        )
                        3 -> ProfileScreen(
                            authViewModel = authViewModel,
                            navController = navController,
                            profileViewModel = profileViewModel
                        )
                    }
                }
            )

        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val middleIndex = tabs.size / 2

                    tabs.take(middleIndex).forEach { item ->
                        TabItem(item, pagerState, scope)
                    }

                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = ""
                    )

                    tabs.drop(middleIndex).forEach { item ->
                        TabItem(item, pagerState, scope)
                    }
                }
            }
        }
    )
}



