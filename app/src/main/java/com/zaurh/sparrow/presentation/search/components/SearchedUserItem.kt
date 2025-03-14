package com.zaurh.sparrow.presentation.search.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zaurh.sparrow.data.models.UserData

@Composable
fun SearchedUserItem(
    userData: UserData,
    onClick: () -> Unit
) {
    Row(Modifier.fillMaxWidth().clickable {
        onClick()
    }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = userData.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(50.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = userData.username)
            Text(text = userData.bio)
        }
    }
}