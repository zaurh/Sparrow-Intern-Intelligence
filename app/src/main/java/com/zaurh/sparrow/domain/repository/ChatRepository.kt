package com.zaurh.sparrow.domain.repository

import com.zaurh.sparrow.data.models.ChatDataWithAuthor
import com.zaurh.sparrow.data.models.MessageData
import com.zaurh.sparrow.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(senderId: String, receiverId: String): Flow<Resource<List<MessageData>>>
    fun sendMessage(messageData: MessageData): Flow<Resource<MessageData>>
    fun getChatList(currentUserId: String): Flow<Resource<List<ChatDataWithAuthor>>>
}