package com.zaurh.sparrow.presentation.comment.state

import com.zaurh.sparrow.data.models.CommentWithAuthor

data class CommentState(
    val postId: String = "",
    val comments: List<CommentWithAuthor> = listOf(),
    val bottomSheetEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val message: String = ""
)