package com.zaurh.sparrow.presentation.account.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.presentation.account.following.state.FollowingState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _followingState = MutableStateFlow(FollowingState())
    val followingState = _followingState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFollowing(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getFollowing(userId).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _followingState.value = followingState.value.copy(
                            followingUsers = result.data ?: listOf()
                        )
                    }
                }
            }
        }

    }
}
