package com.zaurh.sparrow.presentation.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.presentation.comment.CommentBottomSheet
import com.zaurh.sparrow.presentation.comment.CommentViewModel
import com.zaurh.sparrow.shared.ImageContent
import com.zaurh.sparrow.shared.AuthViewModel

@Composable
fun FeedScreen(
    navController: NavController,
    feedViewModel: FeedViewModel = hiltViewModel(),
    commentViewModel: CommentViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    val feedState = feedViewModel.feedState.collectAsState()
    val currentUserState = authViewModel.currentUserState.collectAsState()

    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            val postList = feedState.value.feed
            items(postList){
                ImageContent(
                    postData = it.post,
                    currentUserId = currentUserState.value.data?.userId ?: "",
                    authorImage = it.author.imageUrl,
                    authorUsername = it.author.username,
                    onCommentClick = {
                        commentViewModel.openBottomSheet(it)
                    },
                    onUserClick = {
                        navController.navigate("user_screen/$it")
                    },
                    onLikeClick = { postData ->
                        val currentId = currentUserState.value.data?.userId
                        if (postData.likes.contains(currentId)) {
                            feedViewModel.unlikePost(
                                postId = postData.id,
                                userId = currentId ?: ""
                            )
                        }else{
                            feedViewModel.likePost(
                                postId = postData.id,
                                userId = currentId ?: ""
                            )
                        }
                    }
                )
            }
        }
    }

    CommentBottomSheet(
        commentViewModel = commentViewModel,
        navController = navController,
        authViewModel = authViewModel

    )

}