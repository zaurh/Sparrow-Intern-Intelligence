package com.zaurh.sparrow.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zaurh.sparrow.R
import com.zaurh.sparrow.data.models.PostData
import kotlinx.coroutines.launch

@Composable
fun ProfilePager(
    postList: List<PostData>,
    onPostClick: (String) -> Unit
) {
    val pagerItems = listOf(
        PagerItemData(
            index = 0,
            icon = R.drawable.posts_ic,
            title = "Posts",
            selectedColor = Color.Black,
            unselectedColor = Color.Gray
        ),
        PagerItemData(
            index = 1,
            icon = R.drawable.highlights_ic,
            title = "Highlights",
            selectedColor = Color.Black,
            unselectedColor = Color.Gray
        ),
        PagerItemData(
            index = 2,
            icon = R.drawable.tag_ic,
            title = "Tagged",
            selectedColor = Color.Black,
            unselectedColor = Color.Gray
        )
    )

    val pagerState = rememberPagerState(pageCount = { pagerItems.size })
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        pagerItems.forEach { item ->
            Column(
                modifier = Modifier.clickable {
                    scope.launch {
                        pagerState.animateScrollToPage(item.index)
                    }
                }.padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = "",
                    tint = if (item.index == pagerState.currentPage) item.selectedColor else item.unselectedColor,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    color = if (item.index == pagerState.currentPage) item.selectedColor else item.unselectedColor
                )
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { index ->
        when (index) {
            0 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val images = postList.sortedByDescending { it.timestamp }
                    items(images) { post ->
                        ProfileImageItem(
                            postData = post,
                            onImageClick = { postId ->
                                onPostClick(postId)
                            }
                        )
                    }
                }
            }

            1 -> {
                PagerItem(items = listOf()) {}
            }

            2 -> {
                PagerItem(items = listOf()) {}
            }
        }
    }


}


@Composable
fun PagerItem(
    items: List<PostData>,
    onImageClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        val images = items.sortedByDescending { it.timestamp }
        items(images){ post ->
            ProfileImageItem(
                postData = post,
                onImageClick = { postId ->
                    onImageClick(postId)
                }
            )
        }
    }
}

@Composable
fun ProfileImageItem(
    postData: PostData,
    onImageClick: (String) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(10))
            .clickable {
                onImageClick(postData.id)
            }
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .height(150.dp)
                    .width(120.dp),
                model = postData.image,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
            )

        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = postData.likes.size.toString(), color = Color.White, fontSize = 14.sp)
        }
    }
}