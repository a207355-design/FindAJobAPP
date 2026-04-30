package com.example.findajobapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.components.Header
import com.example.findajobapp.components.JobCard


//收藏屏幕
@Composable
fun FavoriteScreen(viewModel: AppViewModel) {

    //数据获取是从viewModel里取
    val favorites = viewModel.favoriteJobs

    //1.存数据  2，可观察(数据变了UI自动更新)
    var searchText by remember { mutableStateOf("") }
    var locationText by remember { mutableStateOf("") }


    Column {
        Header(
            searchText = searchText,
            onSearchChange = { searchText = it },
            locationText = locationText,
            onLocationChange = { locationText = it }
        )

        Box(modifier = Modifier.fillMaxSize()) {

            val filteredList = favorites.filter { job ->
                job.title.contains(searchText, true) &&
                        job.location.contains(locationText, true)
            }

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No favorites yet")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredList) { job ->

                        val isFav = viewModel.isFavorite(job)

                        JobCard(
                            job = job,
                            isFavorite = isFav,
                            onFavoriteClick = {
                                viewModel.toggleFavorite(job)
                            }
                        )
                    }
                }
            }
        }
    }
}
