package com.zaurh.sparrow.presentation.settings.edit_account.components

import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zaurh.sparrow.presentation.settings.edit_account.EditAccountViewModel

@Composable
fun EditAccountContent(
    modifier: Modifier = Modifier,
    userImage: String,
    editAccountViewModel: EditAccountViewModel,
    onSave: () -> Unit,
    onPictureClick: () -> Unit
) {
    val updateUserState = editAccountViewModel.updateUserState.collectAsState()
    val usernameValue = editAccountViewModel.usernameValue.collectAsState()
    val bioValue = editAccountViewModel.bioValue.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                model = userImage,
                contentDescription = "",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                onClick = {
                    onPictureClick()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
            }

        }
        Spacer(Modifier.height(18.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(10.dp)),
            value = usernameValue.value,
            onValueChange = {
                editAccountViewModel.updateUsernameValue(it)
            },
            label = {
                Text(text = "Username")
            },
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(0.9f).clip(RoundedCornerShape(10.dp)),
            value = bioValue.value,
            onValueChange = {
                editAccountViewModel.updateBioValue(it)
            },
            label = {
                Text(text = "Bio")
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(enabled = !updateUserState.value.isLoading,onClick = {
            onSave()
        }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (updateUserState.value.isLoading) "Saving..." else "Save")
                Spacer(Modifier.width(4.dp))
                if (updateUserState.value.isLoading){
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}