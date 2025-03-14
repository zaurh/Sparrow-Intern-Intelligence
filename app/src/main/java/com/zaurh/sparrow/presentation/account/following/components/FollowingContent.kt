package com.zaurh.sparrow.presentation.account.following.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaurh.sparrow.presentation.account.following.FollowingViewModel
import com.zaurh.sparrow.shared.FollowersItem
import com.zaurh.sparrow.shared.SearchBar

@Composable
fun FollowingContent(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
    followingViewModel: FollowingViewModel
) {
    val followingState = followingViewModel.followingState.collectAsState()
    val searchQuery = followingViewModel.searchQuery.collectAsState()

    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(
            label = "Search",
            query = searchQuery.value,
            onQueryChange = {
                followingViewModel.updateSearchQuery(it)
            }
        )
        LazyColumn {
            items(followingState.value.followingUsers){
                FollowersItem(
                    userData = it
                ) { userId ->
                    onItemClick(userId)
                }
            }
        }
    }
}