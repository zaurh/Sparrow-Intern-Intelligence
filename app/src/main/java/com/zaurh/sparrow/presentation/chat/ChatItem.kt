package com.zaurh.sparrow.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaurh.sparrow.data.models.MessageData
import com.zaurh.sparrow.util.formatTimestamp

@Composable
fun ChatItem(
    messageData: MessageData,
    senderIsCurrentUser: Boolean,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (senderIsCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(
                    if (senderIsCurrentUser) MaterialTheme.colorScheme.surface else Color.Gray.copy(
                        0.3f
                    )
                )
                .padding(8.dp),
            horizontalAlignment = if (senderIsCurrentUser) Alignment.End else Alignment.Start
        ) {
            Text(text = messageData.message)
            Text(
                text = formatTimestamp(messageData.timestamp),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}