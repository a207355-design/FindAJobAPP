package com.example.findajobapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.findajobapp.AppViewModel
import com.example.findajobapp.Message

//消息屏幕
@Composable
fun MessageScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    //⭐ 核心：把原本的假数据，替换成 ViewModel 里的“活数据”
    val messageList = viewModel.activeMessages
    Column(modifier = Modifier.fillMaxSize()) {

        //Header（顶部区域）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFB76E79),
                            Color(0xFFD8A7B1)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            //这是标题
            Text(
                text = "Messages",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        // 滚动列表
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // 遍历每一条动态数据，交给 MessageItem 显示
            items(messageList) { message ->
                MessageItem(
                    message = message,
                    navController = navController,
                    viewModel = viewModel // 👈 传给它
                )
            }
        }
    }
}
@Composable
fun MessageItem(
    message: Message,
    navController: NavController,
    viewModel: AppViewModel // 👈 加在这里
) {
    //外层卡片
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            // ⭐ 关键：让整个卡片可以点击
            .clickable {
                // ⭐ 新增：在跳转之前，先告诉 ViewModel 把这个 HR 的消息标记为已读
                viewModel.markMessageAsRead(message.sender)
                // 发射指令：去 chat 页面，并且带上这个人的名字
                navController.navigate("chat/${message.sender}")
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            // ⚠️ 把之前的 horizontalArrangement = Arrangement.SpaceBetween 删掉，因为我们要用 weight 来控制
            verticalAlignment = Alignment.CenterVertically // 让左右两边垂直居中对齐
        ) {

            // ⭐ 1. 左边的 Column：加上 modifier = Modifier.weight(1f)
            Column(
                modifier = Modifier.weight(1f) // 霸占剩余空间，但会把右边的宽度留出来
            ) {
                Text(
                    text = message.sender,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.lastMessage,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }

            // 为了让左右保持一点距离
            Spacer(modifier = Modifier.width(12.dp))

            // ⭐ 2. 右边的 Column：保持原样，它会自动占用它需要的宽度
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = message.time,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (message.isUnread) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Red, shape = CircleShape)
                    )
                }
            }
        }

    }
}
