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
    // 从 ViewModel 读取聊天记录，就是每个HR都固定的那一句
    val messages = viewModel.chatMessages

    //Scaffold，它直接给你提供了几个“专属槽位”。你只需要把东西往槽位里填就行了：

    //topBar = { ... }：顶部槽位。你把 TopAppBar（聊天室名字和返回按钮）塞进去，它自动帮你固定在屏幕最上面。
    //bottomBar = { ... }：底部槽位。你把输入框和发送按钮塞进去，它自动帮你固定在屏幕最下面。
    //floatingActionButton = { ... }：悬浮按钮槽位（比如常见的右下角那个圆形大加号，虽然你这个页面没用到，但它也自带了这个槽位）。
    Scaffold(
        topBar = {
            TopAppBar(
                // 标题：如果 senderName 是空的，就显示 "Chat"，否则显示对方名字
                title = { Text(text = senderName ?: "Chat", fontWeight = FontWeight.Bold) },
                // 在 TopAppBar 的导航图标里
                //返回按钮
                navigationIcon = {
                    Icon(
                        //Filled：表示你要的是“实心”风格的图标
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, //长什么样
                        //它是专门给视障群体（盲人）使用的“无障碍功能（Accessibility）”
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                //核心：把当前的聊天页从“栈”里弹出，自然就回到了消息列表
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
                //聊天框的样子
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(20.dp)
                )
                //聊天框和提交按钮中间的空隙
                Spacer(modifier = Modifier.width(8.dp))
                //图标按钮容器
                IconButton(
                    onClick = {
                        viewModel.sendMessage(inputText) // 触发 ViewModel 的 ADD 功能
                        inputText = "" // 发送后清空输入框
                    },
                    //粉色
                    modifier = Modifier.background(Color(0xFFD8A7B1), shape = RoundedCornerShape(50))
                ) {
                    //系统自带的图形库
                    Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        // 中间的聊天记录列表
        //Scaffold 就像一个严厉的包工头，它帮你把顶部的标题栏和底部的输入框都盖好了。
        // 然后它扔给你一个innerPadding（内部边距），告诉你：
        //中间的聊天记录只能画在这个安全范围里
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) //不去读写上下空间，让空间变得合理
                .padding(16.dp)
        ) {
            items(messages) { msg ->
                // 判断是不是第一条（对方发的），其他的都是你发的
                //列表里第 0 个位置 (messages[0])，永远是系统默认生成的 HR 开场白
                //所以这里不等于0的时候就是咱们在说
                val isMe = msg != messages[0]

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    // 如果是我 (true) -> 靠右对齐 (End)
                    // 如果是对方 (false) -> 靠左对齐 (Start)
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                // 如果是我 (true) -> 染成主题粉色
                                // 如果是对方 (false) -> 染成浅灰色
                                color = if (isMe) Color(0xFFB76E79) else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = msg,
                            // 粉色背景太深了，所以我的文字用白色 (White)
                            // 灰色背景比较浅，所以对方的文字用黑色 (Black)
                            color = if (isMe) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}