package com.zaurh.sparrow.presentation.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItemData(
    val index: Int,
    val indicator: Int = 0,
    val title: String,
    val icon: ImageVector,
    val unselectedColor: Color = Color.Black,
    val selectedColor: Color = Color.Black
)

val tabs = listOf(
    TabItemData(
        index = 0,
        title = "Feed",
        icon = Icons.Default.Home
    ),
    TabItemData(
        index = 1,
        title = "Search",
        icon = Icons.Default.Search
    ),
    TabItemData(
        index = 2,
        title = "Messages",
        icon = Icons.Default.Email
    ),
    TabItemData(
        index = 3,
        title = "Profile",
        icon = Icons.Default.Person
    )
)