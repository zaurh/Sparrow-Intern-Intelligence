package com.zaurh.sparrow.presentation.chat

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zaurh.sparrow.data.models.MessageData
import com.zaurh.sparrow.shared.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    receiverId: String,
    receiverUsername: String,
    receiverImage: String,
    authViewModel: AuthViewModel,
    navController: NavController,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val currentUserState = authViewModel.currentUserState.collectAsState()
    val messageValue = chatViewModel.messageValue.collectAsState()

    LaunchedEffect(key1 = true) {
        chatViewModel.getMessages(
            senderId = currentUserState.value.data?.userId ?: "",
            receiverId = receiverId
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(modifier = Modifier.clickable {
                        navController.navigate("user_screen/$receiverId")
                    },verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = receiverImage,
                            contentDescription = "",
                            modifier = Modifier.size(40.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text = receiverUsername,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        content = { padding ->
            ChatContent(
                modifier = Modifier.padding(padding),
                chatViewModel = chatViewModel,
                currentUserId = currentUserState.value.data?.userId ?: ""
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent
            ) {
                TextField(
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    value = messageValue.value,
                    onValueChange = {
                        chatViewModel.onMessageValueChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(10))
                        .border(1.dp, color = Color.Black, shape = RoundedCornerShape(10)),
                    placeholder = {
                        Text(text = "Message")
                    },
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        IconButton(onClick = {
                            chatViewModel.sendMessage(
                                messageData = MessageData(
                                    senderId = currentUserState.value.data?.userId ?: "",
                                    receiverId = receiverId,
                                    message = chatViewModel.messageValue.value
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = ""
                            )
                        }
                    }
                )

            }
        }
    )
}