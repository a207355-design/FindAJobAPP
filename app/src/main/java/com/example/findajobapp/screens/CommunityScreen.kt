package com.example.findajobapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.CommunityPost
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CommunityScreen(viewModel: AppViewModel) {
    // 页面一打开，就自动去拉取 Firebase 的数据
    LaunchedEffect(Unit) {
        viewModel.fetchCommunityPosts()
    }

    var inputText by remember { mutableStateOf("") }
    val posts = viewModel.communityPosts

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant)) {
        // 顶部导航栏区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Community", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // 中间：帖子列表 (LazyColumn)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(posts) { post ->
                PostCard(post)
            }
        }

        // 底部：输入框和发送按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Share a job opportunity...") },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.addCommunityPost(inputText)
                    inputText = "" // 发送完清空输入框
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Send")
            }
        }
    }
}

// 单个帖子的 UI 卡片
@Composable
fun PostCard(post: CommunityPost) {
    // 把时间戳转换成能看懂的时间格式 (比如 06-12 14:30)
    val date = Date(post.timestamp)
    val format = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
    val timeString = format.format(date)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = post.author, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(text = timeString, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.content, fontSize = 15.sp)
        }
    }
}