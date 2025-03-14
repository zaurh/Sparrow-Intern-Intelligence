package com.zaurh.sparrow.presentation.account.followers.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zaurh.sparrow.presentation.account.followers.FollowersViewModel
import com.zaurh.sparrow.shared.FollowersItem
import com.zaurh.sparrow.shared.SearchBar

@Composable
fun FollowersContent(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
    followersViewModel: FollowersViewModel
) {
    val followersState = followersViewModel.followersState.collectAsState()
    val searchQuery = followersViewModel.searchQuery.collectAsState()

    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(
            label = "Search",
            query = searchQuery.value,
            onQueryChange = {
                followersViewModel.updateSearchQuery(it)
            }
        )
        LazyColumn {
            items(followersState.value.followers){
                FollowersItem(
                    userData = it
                ) { userId ->
                    onItemClick(userId)
                }
            }
        }
    }
}

