package com.zaurh.sparrow.presentation.chat.state

import com.zaurh.sparrow.data.models.MessageData

data class ChatState(
    val messages: List<MessageData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)