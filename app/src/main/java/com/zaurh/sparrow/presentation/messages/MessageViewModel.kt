package com.zaurh.sparrow.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.domain.repository.ChatRepository
import com.zaurh.sparrow.presentation.messages.state.ChatListState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
): ViewModel() {

    private val _chatListState = MutableStateFlow(ChatListState())
        val chatListState = _chatListState.asStateFlow()


    fun getChatList(currentUserId: String){
        viewModelScope.launch {
            chatRepository.getChatList(currentUserId).collect{result ->
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _chatListState.value = chatListState.value.copy(
                            chatList = result.data ?: emptyList()
                        )
                    }
                }
            }
        }

    }
}