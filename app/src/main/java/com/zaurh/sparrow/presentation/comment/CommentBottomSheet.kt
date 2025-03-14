package com.zaurh.sparrow.presentation.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zaurh.sparrow.data.models.CommentData
import com.zaurh.sparrow.shared.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    commentViewModel: CommentViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val commentState = commentViewModel.commentState.collectAsState()
    val commentValue = commentViewModel.commentValue.collectAsState()
    val currentUserState = authViewModel.currentUserState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(commentState.value.postId) {
        commentViewModel.getComments(postId = commentState.value.postId)
    }

    if (commentState.value.bottomSheetEnabled) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                commentViewModel.closeBottomSheet()
            }
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Comments")
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.fillMaxSize()) {
                    val comments =
                        commentState.value.comments.sortedByDescending { it.comment.timestamp }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(comments) {
                            CommentItem(
                                commentData = it.comment,
                                onCommentClick = {
                                    navController.navigate("user_screen/$it")
                                },
                                authorId = it.author.userId,
                                authorImage = it.author.imageUrl,
                                authorUsername = it.author.username
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)
                    ) {
                        TextField(
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            value = commentValue.value,
                            onValueChange = {
                                commentViewModel.updateCommentValue(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            placeholder = {
                                Text(text = "Add a comment...")
                            },
                            singleLine = true,
                            maxLines = 1,
                            trailingIcon = {
                                IconButton(onClick = {
                                    commentViewModel.addComment(
                                        commentData = CommentData(
                                            authorId = currentUserState.value.data?.userId ?: "",
                                            comment = commentValue.value,
                                            likes = listOf(),
                                            postId = commentState.value.postId
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = ""
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

    }


}




