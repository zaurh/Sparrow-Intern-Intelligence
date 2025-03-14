package com.zaurh.sparrow.presentation.account.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.zaurh.sparrow.presentation.account.user.UserViewModel
import com.zaurh.sparrow.shared.ProfileCard
import com.zaurh.sparrow.shared.ProfilePager
import java.net.URLEncoder

@Composable
fun UserContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentUser: UserData,
    userData: UserData,
    userViewModel: UserViewModel
) {
    val postList = userViewModel.postState.collectAsState()

    var headerHeight by remember { mutableStateOf(200.dp) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newHeight = (headerHeight + delta.dp).coerceIn(0.dp, 200.dp)

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
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(nestedScrollConnection),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileCard(
            modifier = Modifier.height(headerHeight),
            postSize = postList.value.postList.size,
            userData = userData,
            onPostsClick = {
                headerHeight = 0.dp
            },
            onPictureClick = {},
            onFollowersClick = { userId ->
                navController.navigate("followers_screen/$userId")
            },
            onFollowingClick = { userId ->
                navController.navigate("following_screen/$userId")
            },
            onSettingsClick = {
                navController.navigate(Screen.SettingsScreen.route)
            }
        )
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            if (!userData.followers.contains(currentUser.userId)) {
                ProfileButton(title = "Follow", icon = Icons.Default.Person, onClick = {
                    userViewModel.followUser(
                        userId = currentUser.userId,
                        targetUserId = userData.userId,
                        onSuccess = {

                        }
                    )
                })
            }else{
                ProfileButton(title = "Unfollow", icon = Icons.Default.Person, onClick = {
                    userViewModel.unfollowUser(
                        userId = currentUser.userId,
                        targetUserId = userData.userId,
                        onSuccess = {

                        }
                    )
                })
            }

            ProfileButton(title = "Message", icon = Icons.Default.MailOutline, onClick = {
                val userId = userData.userId
                val username = userData.username
                val userImageUrl = URLEncoder.encode(userData.imageUrl, "UTF-8")

                navController.navigate("chat_screen/$userId/$username/$userImageUrl")
            })
        }

        Spacer(Modifier.height(24.dp))

        ProfilePager(
            postList = postList.value.postList,
            onPostClick = { postId ->
                val username = userData.username
                val userImageUrl = URLEncoder.encode(userData.imageUrl, "UTF-8")

                navController.navigate("image_screen/$postId/$username/$userImageUrl")
            }
        )

    }
}