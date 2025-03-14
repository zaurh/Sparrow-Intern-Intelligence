package com.zaurh.sparrow.data.models

import com.google.firebase.Timestamp
import java.util.UUID

data class PostData(
    val id: String = UUID.randomUUID().toString(),
    val authorId: String = "",
    val image: String = "",
    val description: String = "",
    val viewCount: Int = 0,
    val likes: List<String> = listOf(),
    val timestamp: Timestamp = Timestamp.now()
)

data class PostWithAuthor(
    val post: PostData,
    val author: UserData
)
