package com.zaurh.sparrow.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.presentation.search.state.SearchUserState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _searchUserState = MutableStateFlow(SearchUserState())
        val searchUserState = _searchUserState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
        val searchQuery = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    fun updateSearchQuery(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchQuery.value = query
            delay(1000L)
            if(query.isNotEmpty()){
                searchUsers()
            }
        }

    }

    private fun searchUsers(){
        viewModelScope.launch {
            userRepository.searchUsers(searchQuery.value).collect{result ->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _searchUserState.value = SearchUserState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _searchUserState.value = searchUserState.value.copy(
                            isLoading = false,
                            users = result.data ?: emptyList()
                        )
                    }
                }
            }
        }
    }
}