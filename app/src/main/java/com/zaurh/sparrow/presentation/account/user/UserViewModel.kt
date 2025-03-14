package com.zaurh.sparrow.presentation.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.domain.repository.PostRepository
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.presentation.account.user.state.PostState
import com.zaurh.sparrow.presentation.account.user.state.UserDataState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
): ViewModel() {

    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()

    private val _userData = MutableStateFlow(UserDataState())
    val userData = _userData.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        _postState.value = PostState()
    }


    fun getUserData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserData(userId).collect { result ->
                when (result) {
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        _userData.update { it.copy(data = result.data) }
                    }
                }
            }

        }
    }

    fun getUserPosts(
        userId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.getUserPosts(userId).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _postState.value = PostState(postList = result.data ?: listOf())
                    }
                }
            }
        }
    }

    fun followUser(
        userId: String,
        targetUserId: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.followUser(userId, targetUserId).collect { result ->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        onSuccess()
                    }
                }
            }
        }
    }

    fun unfollowUser(
        userId: String,
        targetUserId: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.unfollowUser(userId, targetUserId).collect { result ->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        onSuccess()
                    }
                }
            }
        }
    }

}