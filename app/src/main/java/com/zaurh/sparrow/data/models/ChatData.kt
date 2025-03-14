package com.zaurh.sparrow.data.models

import com.google.firebase.Timestamp

data class ChatData(
    val recipientId: String = "",
    val lastMessage: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

data class ChatDataWithAuthor(
    val userData: UserData,
    val chatData: ChatData
)