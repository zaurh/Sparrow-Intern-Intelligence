package com.zaurh.sparrow.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.domain.repository.PostRepository
import com.zaurh.sparrow.presentation.feed.state.FeedState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _feedState = MutableStateFlow(FeedState())
    val feedState = _feedState.asStateFlow()

    init {
        getFeed()
    }

    private fun getFeed() {
        viewModelScope.launch {
            postRepository.getFeedPosts().collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _feedState.value = _feedState.value.copy(feed = result.data ?: emptyList())
                    }
                }
            }
        }
    }

    fun likePost(
        postId: String,
        userId: String
    ) {
        viewModelScope.launch {
            postRepository.likePost(postId, userId).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {}
                }
            }
        }
    }

    fun unlikePost(
        postId: String,
        userId: String
    ) {
        viewModelScope.launch {
            postRepository.unlikePost(postId, userId).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {}
                }
            }
        }
    }
}