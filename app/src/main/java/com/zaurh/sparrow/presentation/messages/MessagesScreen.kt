package com.zaurh.sparrow.presentation.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.presentation.messages.components.MessageItem
import com.zaurh.sparrow.shared.AuthViewModel
import com.zaurh.sparrow.shared.SearchBar
import java.net.URLEncoder

@Composable
fun MessagesScreen(
    authViewModel: AuthViewModel,
    messageViewModel: MessageViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentUserState = authViewModel.currentUserState.collectAsState()
    val chatListState = messageViewModel.chatListState.collectAsState()

    LaunchedEffect(true) {
        messageViewModel.getChatList(currentUserState.value.data?.userId ?: "")
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar(
            label = "Search",
            query = "",
            onQueryChange = {}
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(chatListState.value.chatList){
                MessageItem(
                    chatDataWithAuthor = it,
                    onChatClick = { chatDataWithAuthor ->
                        val receiverId = chatDataWithAuthor.userData.userId
                        val receiverUsername = chatDataWithAuthor.userData.username
                        val receiverImage = URLEncoder.encode(chatDataWithAuthor.userData.imageUrl, "UTF-8")

                        navController.navigate("chat_screen/$receiverId/$receiverUsername/$receiverImage")
                    }
                )
            }
        }
    }
}

