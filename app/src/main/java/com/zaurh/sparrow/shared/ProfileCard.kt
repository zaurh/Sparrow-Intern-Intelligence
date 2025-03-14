package com.zaurh.sparrow.shared

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zaurh.sparrow.data.models.UserData
import java.io.File

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    userData: UserData,
    postSize: Int = 0,
    onPictureClick: () -> Unit,
    onPostsClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onFollowersClick: (String) -> Unit,
    onFollowingClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    spotColor = Color.Black.copy(alpha = 0.4f),
                    ambientColor = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)

        ) {
            Row {
                AsyncImage(
                    model = userData.imageUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .height(120.dp)
                        .width(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            onPictureClick()
                        },
                    contentScale = ContentScale.Crop
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = userData.username,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(text = userData.bio, color = Color.Gray, fontSize = 14.sp)
                            }
                            IconButton(onClick = {
                                onSettingsClick()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ProfileAboutCard("Posts", postSize) {
                                onPostsClick()
                            }
                            ProfileAboutCard("Followers", userData.followers.size) {
                                onFollowersClick(userData.userId)
                            }
                            ProfileAboutCard("Following", userData.following.size) {
                                onFollowingClick(userData.userId)
                            }
                        }
                    }
                }
            }
        }
    }

}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "selected_image.jpg") // Create temp file
    file.outputStream().use { output ->
        inputStream?.copyTo(output) // Copy data
    }
    return file
}

@Composable
fun ProfileAboutCard(
    title: String,
    count: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .clickable {
                onClick()
            }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(count.toString(), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}