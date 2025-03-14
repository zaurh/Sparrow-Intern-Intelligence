package com.zaurh.sparrow.presentation.feed.state

import com.zaurh.sparrow.data.models.PostWithAuthor

data class FeedState(
    val feed: List<PostWithAuthor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)