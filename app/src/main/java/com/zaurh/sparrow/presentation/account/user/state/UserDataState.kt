package com.zaurh.sparrow.presentation.account.user.state

import com.zaurh.sparrow.data.models.UserData

data class UserDataState(
    val loading: Boolean = false,
    val data: UserData? = null,
    val error: String? = null
)