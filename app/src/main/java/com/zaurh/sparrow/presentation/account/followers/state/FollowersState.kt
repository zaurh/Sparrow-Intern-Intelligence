package com.zaurh.sparrow.presentation.account.followers.state

import com.zaurh.sparrow.data.models.UserData

data class FollowersState(
    val followers: List<UserData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)