package com.zaurh.sparrow.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TabItem(item: TabItemData, pagerState: PagerState, scope: CoroutineScope) {
    Column(
        modifier = Modifier.clickable {
            scope.launch {
                pagerState.animateScrollToPage(item.index)
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = "",
            tint = if (item.index == pagerState.currentPage) item.selectedColor else item.unselectedColor
        )
        Text(
            text = item.title,
            fontSize = 14.sp,
            color = if (item.index == pagerState.currentPage) item.selectedColor else item.unselectedColor
        )
    }
}