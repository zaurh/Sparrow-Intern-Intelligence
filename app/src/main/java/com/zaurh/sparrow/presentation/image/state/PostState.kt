package com.zaurh.sparrow.presentation.image.state

import com.zaurh.sparrow.data.models.PostData

data class PostState(
    val post: PostData? = null,
    val isLoading: Boolean = false,
    val message: String = ""
)