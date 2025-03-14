package com.zaurh.sparrow.presentation.account.user.state

import com.zaurh.sparrow.data.models.PostData

data class PostState(
    val postList: List<PostData> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedPost: PostData? = null
)