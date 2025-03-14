package com.zaurh.sparrow.presentation.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    currentUserId: String
) {
    val chatState = chatViewModel.chatState.collectAsState()
    val lazyState = rememberLazyListState()

    LaunchedEffect(key1 = chatState.value.messages.size) {
        lazyState.scrollToItem(chatState.value.messages.size)
    }

    Column(
        modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = lazyState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(chatState.value.messages) {
                ChatItem(
                    messageData = it,
                    senderIsCurrentUser = it.senderId == currentUserId
                )
            }
        }


    }
}