package com.zaurh.sparrow.presentation.auth.sign_in.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInTopBar() {
    TopAppBar(
        title = { Text(text = "Sign in") }
    )
}