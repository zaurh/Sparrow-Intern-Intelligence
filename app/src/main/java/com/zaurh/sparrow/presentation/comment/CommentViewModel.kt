package com.zaurh.sparrow.presentation.comment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.data.models.CommentData
import com.zaurh.sparrow.domain.repository.CommentRepository
import com.zaurh.sparrow.presentation.comment.state.CommentState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository
): ViewModel() {

    private val _commentState = MutableStateFlow(CommentState())
    val commentState = _commentState.asStateFlow()

    private val _commentValue = MutableStateFlow("")
    val commentValue = _commentValue.asStateFlow()


    fun openBottomSheet(postId: String){
        _commentState.update {
            it.copy(postId = postId,bottomSheetEnabled = true)
        }
    }

    fun closeBottomSheet(){
        _commentState.update {
            it.copy(bottomSheetEnabled = false)
        }
    }

    fun addComment(commentData: CommentData){
        _commentValue.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            commentRepository.addComment(commentData).collect{ result ->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {}
                }
            }
        }
    }

    fun getComments(postId: String){
        viewModelScope.launch(Dispatchers.IO) {
            commentRepository.getComments(postId).collect { result ->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _commentState.update { it.copy(comments = result.data ?: listOf()) }
                    }
                }
            }
        }
    }

    fun updateCommentValue(value: String){
        _commentValue.value = value
    }

}