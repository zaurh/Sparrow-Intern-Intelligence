package com.zaurh.sparrow.domain.repository

import android.content.Context
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    fun getUserData(uid: String): Flow<Resource<UserData>>
    fun getFollowers(userId: String): Flow<Resource<List<UserData>>>
    fun getFollowing(userId: String): Flow<Resource<List<UserData>>>
    fun followUser(userId: String, targetUserId: String): Flow<Resource<String>>
    fun unfollowUser(userId: String, targetUserId: String): Flow<Resource<String>>
    fun updateUserData(userData: UserData): Flow<Resource<UserData>>
    fun uploadImage(file: File, context: Context): Flow<Resource<String>>

    fun searchUsers(username: String): Flow<Resource<List<UserData>>>
}