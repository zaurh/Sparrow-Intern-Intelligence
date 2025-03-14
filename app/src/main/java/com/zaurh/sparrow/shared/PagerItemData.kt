package com.zaurh.sparrow.shared

import androidx.compose.ui.graphics.Color

data class PagerItemData(
    val index: Int,
    val icon: Int,
    val title: String,
    val selectedColor: Color,
    val unselectedColor: Color
)