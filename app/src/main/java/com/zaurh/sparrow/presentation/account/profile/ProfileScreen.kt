package com.zaurh.sparrow.presentation.account.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.navigation.Screen
import com.zaurh.sparrow.shared.AuthViewModel
import com.zaurh.sparrow.shared.ProfileCard
import com.zaurh.sparrow.shared.ProfilePager
import java.net.URLEncoder


@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {
    val userData = authViewModel.currentUserState.collectAsState()
    val postList = profileViewModel.postState.collectAsState()

    LaunchedEffect(userData.value.data?.userId) {
        userData.value.data?.userId?.let {
            profileViewModel.getUserPosts(it)
        }
    }

    DisposableEffect(true) {
        onDispose {
            profileViewModel.clearPosts()
        }
    }


    var headerHeight by remember { mutableStateOf(180.dp) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newHeight = (headerHeight + delta.dp).coerceIn(0.dp, 180.dp)

                return if (newHeight != headerHeight) {
                    headerHeight = newHeight
                    Offset(0f, delta)
                } else {
                    Offset.Zero
                }
            }
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(nestedScrollConnection),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileCard(
            modifier = Modifier.height(headerHeight),
            userData = userData.value.data ?: UserData(),
            postSize = postList.value.postList.size,
            onPostsClick = {
                headerHeight = 0.dp
            },
            onSettingsClick = {
                navController.navigate(Screen.SettingsScreen.route)
            },
            onPictureClick = {

            },
            onFollowersClick = { userId ->
                navController.navigate("followers_screen/$userId")
            },
            onFollowingClick = { userId ->
                navController.navigate("following_screen/$userId")
            }
        )

        Spacer(Modifier.height(24.dp))

        ProfilePager(
            postList = postList.value.postList,
            onPostClick = { postId ->
                val username = userData.value.data?.username ?: ""
                val userImageUrl = URLEncoder.encode(userData.value.data?.imageUrl, "UTF-8")

                navController.navigate("image_screen/$postId/$username/$userImageUrl")
            }
        )

    }
}







