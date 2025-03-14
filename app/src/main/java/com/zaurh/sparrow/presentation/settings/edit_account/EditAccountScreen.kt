package com.zaurh.sparrow.presentation.settings.edit_account

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.presentation.settings.edit_account.components.EditAccountContent
import com.zaurh.sparrow.shared.AuthViewModel
import com.zaurh.sparrow.shared.uriToFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    editAccountViewModel: EditAccountViewModel = hiltViewModel()
) {
    val currentUser = authViewModel.currentUserState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null){
            editAccountViewModel.uploadImage(
                file = uriToFile(context, uri),
                context = navController.context,
                onSuccess = { imageUrl ->
                    currentUser.value.data?.let {
                        editAccountViewModel.updateUserData(
                            userData = it.copy(imageUrl = imageUrl),
                            onSuccess = {
                                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            )
        }

    }

    LaunchedEffect(currentUser.value.data) {
        editAccountViewModel.updateUsernameValue(currentUser.value.data?.username ?: "")
        editAccountViewModel.updateBioValue(currentUser.value.data?.bio ?: "")
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Text(text = "Edit Account")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        content = { padding ->
            val imageUrl = currentUser.value.data?.imageUrl ?: ""
            EditAccountContent(
                modifier = Modifier.padding(padding),
                userImage = imageUrl,
                editAccountViewModel = editAccountViewModel,
                onSave = {
                    val userData = currentUser.value.data?.copy(
                        username = editAccountViewModel.usernameValue.value,
                        bio = editAccountViewModel.bioValue.value
                    )
                    if (userData != null) {
                        editAccountViewModel.updateUserData(userData) {
                            navController.popBackStack()
                        }
                    }
                },
                onPictureClick = {
                    launcher.launch("image/*")
                }
            )
        }
    )
}