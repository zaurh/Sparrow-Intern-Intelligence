package com.zaurh.sparrow.ui.theme

import androidx.compose.ui.graphics.Color

sealed class ThemeColors(
    val surface: Color,
    val background: Color,
    val text: Color
) {
    object Night : ThemeColors(
        surface = Color(0xFF070420),
        background = Color(0xFFFFF9E8),
        text = Color.White
    )

    object Day : ThemeColors(
        surface = Color(0xFFF5E6FF),
        background = Color(0xFFE8FFF8),
        text = Color(0xFF070420)
    )

}