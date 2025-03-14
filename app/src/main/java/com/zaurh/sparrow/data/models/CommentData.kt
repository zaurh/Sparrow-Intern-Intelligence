package com.zaurh.sparrow.data.models

import com.google.firebase.Timestamp
import java.util.UUID

data class CommentData(
    val id: String = UUID.randomUUID().toString(),
    val postId: String = "",
    val authorId: String = "",
    val comment: String = "",
    val likes: List<String> = listOf(),
    val timestamp: Timestamp = Timestamp.now()
)

data class CommentWithAuthor(
    val comment: CommentData,
    val author: UserData
)