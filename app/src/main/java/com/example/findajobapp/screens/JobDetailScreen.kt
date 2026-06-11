package com.example.findajobapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.jobList

@Composable
fun JobDetailScreen(
    navController: NavController,
    viewModel: AppViewModel,
    companyName: String?
) {
    val job = jobList.find { it.company == companyName }

    LaunchedEffect(key1 = companyName) {
        if (companyName != null) {
            viewModel.fetchCompanyNews(companyName)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. 自定义渐变头部 (与主页风格统一)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Job Details",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 2. 职位内容与新闻列表
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 职位详细信息
            item {
                if (job != null) {
                    Text(text = job.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Company: ${job.company}", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Location: ${job.location}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Salary: ${job.salary}", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF2E7D32))
                    Text(text = "Posted: ${job.time}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = "Latest News about $companyName",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 加载新闻部分
            if (viewModel.isNewsLoading.value) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (viewModel.newsErrorMessage.value.isNotEmpty()) {
                item {
                    Text(text = viewModel.newsErrorMessage.value, color = Color.Red, modifier = Modifier.padding(8.dp))
                }
            } else {
                items(viewModel.companyNews) { article ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = article.title ?: "No Title",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = article.description ?: "No description available.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                                maxLines = 3
                            )
                        }
                    }
                }
            }
        }
    }
}