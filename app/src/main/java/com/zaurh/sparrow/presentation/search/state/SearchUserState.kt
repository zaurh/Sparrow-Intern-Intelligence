package com.zaurh.sparrow.presentation.search.state

import com.zaurh.sparrow.data.models.UserData

data class SearchUserState(
    val isLoading: Boolean = false,
    val users: List<UserData> = emptyList(),
    val error: String = ""
)
