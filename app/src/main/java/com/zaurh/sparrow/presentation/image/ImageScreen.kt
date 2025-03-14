package com.zaurh.sparrow.presentation.image

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.data.models.PostData
import com.zaurh.sparrow.presentation.comment.CommentBottomSheet
import com.zaurh.sparrow.presentation.comment.CommentViewModel
import com.zaurh.sparrow.shared.AuthViewModel
import com.zaurh.sparrow.shared.ImageContent
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(
    authorUsername: String,
    authorImageUrl: String,
    postId: String,
    navController: NavController,
    authViewModel: AuthViewModel,
    imageViewModel: ImageViewModel = hiltViewModel(),
    commentViewModel: CommentViewModel = hiltViewModel()
) {
    val currentUserState = authViewModel.currentUserState.collectAsState()
    val postData = imageViewModel.postState.collectAsState()

    val authorImage = URLDecoder.decode(
        authorImageUrl,
        "UTF-8"
    )

    LaunchedEffect(true) {
        imageViewModel.getSpecificPost(postId = postId)
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = { Text(text = "Posts") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        content = { padding ->
            ImageContent(
                modifier = Modifier.padding(padding),
                postData = postData.value.post ?: PostData(),
                authorImage = authorImage,
                authorUsername = authorUsername,
                currentUserId = currentUserState.value.data?.userId ?: "",
                onCommentClick = {
                    commentViewModel.openBottomSheet(it)
                },
                onUserClick = {
                    navController.navigate("user_screen/$it")
                },
                onLikeClick = { postData ->
                    val currentId = currentUserState.value.data?.userId
                    if (postData.likes.contains(currentId)) {
                        imageViewModel.unlikePost(
                            postId = postData.id,
                            userId = currentId ?: ""
                        )
                    } else {
                        imageViewModel.likePost(
                            postId = postData.id,
                            userId = currentId ?: ""
                        )
                    }
                }
            )


        }
    )

    CommentBottomSheet(
        navController = navController,
        commentViewModel = commentViewModel,
        authViewModel = authViewModel
    )

}