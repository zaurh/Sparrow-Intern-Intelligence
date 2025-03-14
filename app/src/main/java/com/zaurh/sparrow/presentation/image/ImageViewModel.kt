package com.zaurh.sparrow.presentation.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.domain.repository.PostRepository
import com.zaurh.sparrow.presentation.image.state.PostState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImageViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()


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

    fun getSpecificPost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.getSpecificPost(postId).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _postState.update { it.copy(post = result.data) }
                    }
                }
            }
        }
    }


}