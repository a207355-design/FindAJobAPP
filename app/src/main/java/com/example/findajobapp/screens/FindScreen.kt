package com.example.findajobapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.components.FilterBar
import com.example.findajobapp.components.Header
import com.example.findajobapp.components.JobCard
import com.example.findajobapp.jobList

@Composable
fun FindScreen(viewModel: AppViewModel, navController: NavController) {

    val favoriteJobs by viewModel.favoriteJobs.collectAsState()
    val listState = rememberLazyListState()

    val searchText = viewModel.searchText.value
    val locationText = viewModel.locationText.value

    var selectedChip by remember { mutableStateOf("") }
    var selectedRecruiter by remember { mutableStateOf("") }
    var selectedJobType by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedSalary by remember { mutableStateOf("") }

    Column {
        Header(
            searchText = searchText,
            onSearchChange = { viewModel.updateSearch(it) },
            locationText = locationText,
            onLocationChange = { viewModel.updateLocation(it) }
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            val filteredList = jobList.filter { job ->
                val matchSearch = job.title.contains(searchText, true) || job.company.contains(searchText, true)

                // 核心修复：模糊地点匹配
                val locationQuery = locationText.trim()
                val matchLocation = if (locationQuery.isEmpty()) {
                    true
                } else {
                    // 如果搜索栏里有逗号，取第一个词（比如把 "Bangi, Selangor" 变成 "Bangi"）
                    val query = locationQuery.split(",")[0].trim()
                    // 只要工作地点里包含这个词就匹配
                    job.location.contains(query, ignoreCase = true)
                }

                val matchJobType = if (selectedJobType.isEmpty()) true
                // 这里有个小逻辑：原来的代码是直接匹配"Remote"，建议根据选中的类型判断
                else if (selectedJobType == "Remote") job.location.contains("Remote", true)
                else true // "Full-time" 等其他逻辑可以在这儿细化

                val matchDate = if (selectedDate.isEmpty()) true
                else if (selectedDate == "Hours") job.time.contains("hour", true)
                else job.time.contains("day", true)

                val matchSalary = if (selectedSalary.isEmpty()) true else {
                    val salaryNum = job.salary.replace("K", "").replace("–", "-")
                        .split("-").getOrNull(0)?.toIntOrNull() ?: 0
                    if (selectedSalary == "10K") salaryNum >= 10 else salaryNum >= 20
                }

                val matchQuickChip = if (selectedChip.isEmpty()) true else {
                    when (selectedChip) {
                        "Date Posted" -> job.time.contains("hour", true)
                        "Job types" -> job.location.contains("Remote", true)
                        "Salary" -> {
                            val salaryNum = job.salary.replace("K", "").split("–").getOrNull(0)?.toIntOrNull() ?: 0
                            salaryNum >= 15
                        }
                        else -> true
                    }
                }
                matchSearch && matchLocation && matchJobType && matchDate && matchSalary && matchQuickChip
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 60.dp, bottom = 16.dp)
            ) {
                items(filteredList) { job ->
                    val isFav = favoriteJobs.any { it.title == job.title && it.company == job.company }

                    JobCard(
                        job = job,
                        isFavorite = isFav,
                        onFavoriteClick = { viewModel.toggleFavorite(job) },
                        // 点击卡片，跳入详情页
                        onClick = { navController.navigate("jobDetail/${job.company}") }
                    )
                }
            }

            FilterBar(
                selectedChip = selectedChip,
                onChipClick = { selectedChip = if (selectedChip == it) "" else it },
                selectedRecruiter = selectedRecruiter,
                onRecruiterChange = { selectedRecruiter = it },
                selectedJobType = selectedJobType,
                onJobTypeChange = { selectedJobType = it },
                selectedDate = selectedDate,
                onDateChange = { selectedDate = it },
                selectedSalary = selectedSalary,
                onSalaryChange = { selectedSalary = it },
                onLocationChange = { viewModel.updateLocation(it) },
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}