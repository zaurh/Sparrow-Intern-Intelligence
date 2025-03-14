package com.zaurh.sparrow.presentation.account.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.data.models.PostData
import com.zaurh.sparrow.domain.repository.PostRepository
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _postState = MutableStateFlow(PostState())
    val postState = _postState.asStateFlow()


    fun addPost(postData: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.addPost(postData).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {}
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

    fun uploadImage(
        file: File,
        context: Context,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.uploadImage(file, context).collect { result ->
                when (result) {
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        result.data?.let {
                            onSuccess(it)
                        }
                    }

                }
            }
        }

    }

    fun clearPosts() {
        _postState.update {
            it.copy(postList = listOf())
        }

    }
}