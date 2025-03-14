package com.zaurh.sparrow.presentation.messages.state

import com.zaurh.sparrow.data.models.ChatDataWithAuthor

data class ChatListState(
    val chatList: List<ChatDataWithAuthor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)