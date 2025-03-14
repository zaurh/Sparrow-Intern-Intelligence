package com.zaurh.sparrow.presentation.messages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zaurh.sparrow.data.models.ChatDataWithAuthor
import com.zaurh.sparrow.util.formatTimestamp

@Composable
fun MessageItem(
    chatDataWithAuthor: ChatDataWithAuthor,
    onChatClick: (ChatDataWithAuthor) -> Unit
) {
    Row(Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
        .clickable {
            onChatClick(chatDataWithAuthor)
        }
        .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = chatDataWithAuthor.userData.imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = chatDataWithAuthor.userData.username)
                Text(chatDataWithAuthor.chatData.lastMessage)
            }
        }
        Text(formatTimestamp(chatDataWithAuthor.chatData.timestamp))

    }
}