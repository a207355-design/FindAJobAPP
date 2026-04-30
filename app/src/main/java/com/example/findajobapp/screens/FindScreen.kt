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
            //用户输入：改数据
            onSearchChange = { viewModel.updateSearch(it) },
            locationText = locationText,
            //用户输入：改数据
            onLocationChange = { viewModel.updateLocation(it) }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

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
                //渲染job列表
                items(filteredList) { job ->
                    //判断这个job是否是收藏状态
                    val isFav = viewModel.isFavorite(job)

                    JobCard(
                        job = job,
                        isFavorite = isFav,
                        //点击爱心修改状态
                        onFavoriteClick = {
                            viewModel.toggleFavorite(job)
                        }
                    )
                }
            }

            FilterBar(
                selectedChip = selectedChip,
                onChipClick = {
                    selectedChip = if (selectedChip == it) "" else it
                },
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
