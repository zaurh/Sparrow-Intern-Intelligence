package com.zaurh.sparrow.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zaurh.sparrow.R
import com.zaurh.sparrow.data.models.PostData

@Composable
fun ImageContent(
    modifier: Modifier = Modifier,
    currentUserId: String,
    postData: PostData,
    authorImage: String,
    authorUsername: String,
    onCommentClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onLikeClick: (PostData) -> Unit
) {

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.clickable {
                onUserClick(postData.authorId)
            }, verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = authorImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
                Text(text = authorUsername)
            }
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
            }
        }
        AsyncImage(
            model = postData.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onLikeClick(postData)
                }) {
                    if (postData.likes.contains(currentUserId)) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "",
                            tint = Color.Red
                        )
                    } else {
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "")
                    }
                }
                Text(text = postData.likes.size.toString())
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = {
                    onCommentClick(postData.id)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.comment_ic),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp),
                        tint = Color.Black
                    )
                }

            }
            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(R.drawable.saved_ic),
                    contentDescription = "",
                    modifier = Modifier.size(22.dp),
                    tint = Color.Black
                )
            }
        }
    }
}