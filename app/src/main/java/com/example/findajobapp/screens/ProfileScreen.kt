package com.example.findajobapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findajobapp.AppViewModel


//个人资料屏幕
@Composable
fun ProfileScreen(viewModel: AppViewModel) {

    //控制现在是查看模式还是编辑模式
    var isEditing by remember { mutableStateOf(false) }
    //定义 user 变量，从 ViewModel 的 profile 中取出当前的状态值
    val user = viewModel.profile.value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        //Header（固定高度）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                //固定高度
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
            Text(
                text = "Profile",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // 🔥 关键：让下面区域占满剩余空间
        Card(
            modifier = Modifier
                //占满剩余空间
                .weight(1f) // ⭐ 核心！！！
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize() // ⭐ 让内容撑满 Card
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween // ⭐ 拉开上下
            ) {

                Column {
                    //根据 isEditing 决定显示 Text 或 TextField
                    EditableItem(
                        label = "Name",
                        value = user.name, // 对应 viewModel.profile.value.name
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateName(it) }
                    )

                    EditableItem(
                        label = "Gender",
                        value = user.gender,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateGender(it) }
                    )

                    EditableItem(
                        label = "Age",
                        value = user.age,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateAge(it) }
                    )

                    EditableItem(
                        label = "Location",
                        value = user.location,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateProfileLocation(it) }
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    EditableItem(
                        label = "Email",
                        value = user.email,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updateEmail(it) }
                    )

                    EditableItem(
                        label = "Phone",
                        value = user.phone,
                        isEditing = isEditing,
                        onValueChange = { viewModel.updatePhone(it) }
                    )
                }

                //可以放一个按钮（更像真实App）开始进去默认是false，所以显示修改账户
                Text(
                    text = if (isEditing) "Save" else "Edit Profile",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            isEditing = !isEditing
                        }
                )
            }
        }
    }
}

@Composable
fun EditableItem(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray
        )

        //如果isEditing的值是true，就在这里弄
        if (isEditing) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true
            )
        } else {
            Text(
                text = value,
                fontWeight = FontWeight.Bold
            )
        }
    }
}