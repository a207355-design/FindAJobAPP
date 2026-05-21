package com.example.findajobapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // 🚨 必须导入这个
import androidx.compose.runtime.getValue      // 🚨 必须导入这个
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.Job            // 🚨 必须导入普通的 Job
import com.example.findajobapp.components.Header
import com.example.findajobapp.components.JobCard

// 收藏屏幕
@Composable
fun FavoriteScreen(viewModel: AppViewModel) {

    // 🚨 关键修改 1：把水管接通，转换成 Compose 能认识的实时状态！
    val favorites by viewModel.favoriteJobs.collectAsState()

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

            // 过滤逻辑
            val filteredList = favorites.filter { favJob ->
                favJob.title.contains(searchText, true) &&
                        favJob.location.contains(locationText, true)
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
                    items(filteredList) { favJob ->

                        // 🚨 关键修改 2：把数据库的 FavoriteJob 转换回卡片认识的 Job
                        val job = Job(
                            title = favJob.title,
                            company = favJob.company,
                            location = favJob.location,
                            time = favJob.time,
                            salary = favJob.salary
                        )

                        // 既然它已经在收藏列表里了，那肯定是点亮状态
                        val isFav = true

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
