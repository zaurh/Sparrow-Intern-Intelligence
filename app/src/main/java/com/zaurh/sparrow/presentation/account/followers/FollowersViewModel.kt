package com.zaurh.sparrow.presentation.account.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.presentation.account.followers.state.FollowersState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){

    private val _followersState = MutableStateFlow(FollowersState())
        val followersState = _followersState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
        val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String){
        _searchQuery.value = query
    }

    fun getFollowers(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getFollowers(userId).collect {result ->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _followersState.value = followersState.value.copy(
                            followers = result.data ?: listOf()
                        )
                    }
                }
            }
        }
    }


}