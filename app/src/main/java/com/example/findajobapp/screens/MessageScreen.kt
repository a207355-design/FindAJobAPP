package com.example.findajobapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.data.ChatMessage

@Composable
fun MessageScreen(navController: NavController, viewModel: AppViewModel) {
    // 自动监视数据库，有新消息会自动刷新
    val messageList by viewModel.activeMessages.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Brush.horizontalGradient(listOf(Color(0xFFB76E79), Color(0xFFD8A7B1)))), contentAlignment = Alignment.Center) {
            Text(text = "Messages", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(messageList) { message ->
                MessageItem(message = message, navController = navController)
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp).clickable {
            // 点击跳转到聊天界面，传递 HR 名字
            navController.navigate("chat/${message.hrName}")
        },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                // 使用数据库中的真实字段 hrName 和 content
                Text(text = message.hrName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = message.content, color = Color.Gray, maxLines = 2)
            }
        }
    }
}