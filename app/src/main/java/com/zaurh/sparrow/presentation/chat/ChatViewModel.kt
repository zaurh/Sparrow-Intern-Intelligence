package com.zaurh.sparrow.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.data.models.MessageData
import com.zaurh.sparrow.domain.repository.ChatRepository
import com.zaurh.sparrow.presentation.chat.state.ChatState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private val _messageValue = MutableStateFlow("")
    val messageValue = _messageValue.asStateFlow()

    fun getMessages(senderId: String, receiverId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(senderId, receiverId).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _chatState.value = chatState.value.copy(
                            messages = result.data ?: emptyList()
                        )
                    }
                }

            }
        }
    }

    fun sendMessage(messageData: MessageData) {
        _messageValue.value = ""
        viewModelScope.launch {
            chatRepository.sendMessage(messageData).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {}
                }
            }
        }
    }

    fun onMessageValueChange(newValue: String) {
        _messageValue.value = newValue
    }


}