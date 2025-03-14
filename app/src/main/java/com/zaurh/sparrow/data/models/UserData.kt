package com.zaurh.sparrow.data.models

data class UserData(
    val userId: String = "",
    val username: String = "",
    val bio: String = "",
    val email: String = "",
    val password: String = "",
    val imageUrl: String = "",
    val followers: List<String> = listOf(),
    val following: List<String> = listOf(),
    val chatList: List<ChatData> = listOf()
)
