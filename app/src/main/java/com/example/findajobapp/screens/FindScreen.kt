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
import androidx.compose.runtime.collectAsState // 🚨 必须导入
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.components.FilterBar
import com.example.findajobapp.components.Header
import com.example.findajobapp.components.JobCard
import com.example.findajobapp.jobList

//找工作屏幕的UI
@Composable
fun FindScreen(viewModel: AppViewModel) {

    // 🚨 关键修改：告诉这个界面随时盯着数据库。这样点爱心时才能瞬间刷新！
    val favoriteJobs by viewModel.favoriteJobs.collectAsState()

    //记住你滑到了哪里，返回的时候还能在那里
    val listState = rememberLazyListState()

    //UI从viwmodel里面读取数据
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
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            //过滤逻辑
            val filteredList = jobList.filter { job ->
                val matchSearch = job.title.contains(searchText, true)
                val matchLocation = job.location.contains(locationText, true)

                val matchJobType = if (selectedJobType.isEmpty()) true
                else job.location.contains("Remote", true)

                val matchDate = if (selectedDate.isEmpty()) true
                else if (selectedDate == "Hours") job.time.contains("hour", true)
                else job.time.contains("day", true)

                val matchSalary = if (selectedSalary.isEmpty()) true else {
                    val salaryNum = job.salary.replace("K", "")
                        .split("–")
                        .getOrNull(0)
                        ?.toIntOrNull() ?: 0

                    if (selectedSalary == "10K") salaryNum >= 10
                    else salaryNum >= 20
                }

                val matchQuickChip = if (selectedChip.isEmpty()) true else {
                    when (selectedChip) {
                        "Date Posted" -> job.time.contains("hour", true)
                        "Job types" -> job.location.contains("Remote", true)
                        "Salary" -> {
                            val salaryNum = job.salary.replace("K", "")
                                .split("–").getOrNull(0)?.toIntOrNull() ?: 0
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
                    // 🚨 关键修改：直接用我们刚才订阅的流状态来判断是否收藏，确保 100% 灵敏
                    val isFav = favoriteJobs.any { it.title == job.title && it.company == job.company }

                    JobCard(
                        job = job,
                        isFavorite = isFav,
                        onFavoriteClick = {
                            viewModel.toggleFavorite(job)
                        }
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
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
