package com.zaurh.sparrow.data.models

import com.google.firebase.Timestamp
import java.util.UUID

data class MessageData(
    val messageId: String = UUID.randomUUID().toString(),
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

