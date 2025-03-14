package com.zaurh.sparrow.presentation.account.following.state

import com.zaurh.sparrow.data.models.UserData

data class FollowingState(
    val followingUsers: List<UserData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)