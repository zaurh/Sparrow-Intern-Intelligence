package com.zaurh.sparrow.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zaurh.sparrow.presentation.search.components.SearchedUserItem
import com.zaurh.sparrow.shared.SearchBar

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery = searchViewModel.searchQuery.collectAsState()
    val searchUserState = searchViewModel.searchUserState.collectAsState()

    BackHandler(
        onBack = {
            if (searchQuery.value.isNotEmpty()){
                searchViewModel.updateSearchQuery("")
            }else{
                navController.popBackStack()
            }
        }
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar(
            label = "Search for users",
            query = searchQuery.value,
            onQueryChange = {
                searchViewModel.updateSearchQuery(it)
            }
        )
        Spacer(Modifier.height(12.dp))
        if (searchUserState.value.isLoading){
            CircularProgressIndicator()
        }
        LazyColumn {
            val searchedUsers = searchUserState.value.users
            items(searchedUsers){
                SearchedUserItem(userData = it, onClick = {
                    navController.navigate("user_screen/${it.userId}")
                })
            }
        }
    }
}

