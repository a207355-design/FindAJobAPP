package com.example.findajobapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.findajobapp.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: AppViewModel,
    senderName: String? // 接收外面传进来的名字，比如 "Google HR"
) {
    // 记住用户在输入框里打的字
    var inputText by remember { mutableStateOf("") }
    // 从 ViewModel 读取聊天记录
    val messages = viewModel.chatMessages

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = senderName ?: "Chat", fontWeight = FontWeight.Bold) },
                // 在 TopAppBar 的导航图标里
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                // ⭐ 核心：把当前的聊天页从“栈”里弹出，自然就回到了消息列表
                                navController.popBackStack()
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD8A7B1),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // 底部的输入框和发送按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        viewModel.sendMessage(inputText) // 触发 ViewModel 的 ADD 功能
                        inputText = "" // 发送后清空输入框
                    },
                    modifier = Modifier.background(Color(0xFFD8A7B1), shape = RoundedCornerShape(50))
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        // 中间的聊天记录列表
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(messages) { msg ->
                // 判断是不是第一条（对方发的），其他的都是你发的
                val isMe = msg != messages[0]

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isMe) Color(0xFFB76E79) else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(text = msg, color = if (isMe) Color.White else Color.Black)
                    }
                }
            }
        }
    }
}