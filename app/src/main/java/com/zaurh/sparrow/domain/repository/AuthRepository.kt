package com.zaurh.sparrow.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.zaurh.sparrow.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String) : Flow<Resource<FirebaseUser?>>
    suspend fun signIn(email: String, password: String) : Flow<Resource<FirebaseUser?>>
    fun getCurrentUser(): FirebaseUser?
    fun signOut()
    fun addAuthStateListener(listener: (FirebaseUser?) -> Unit)
}