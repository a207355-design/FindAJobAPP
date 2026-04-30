package com.example.findajobapp.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findajobapp.R
import com.example.findajobapp.R.drawable.search

//搜索部分的内容
@Composable
fun Search(
    query: String,
    onQueryChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val image = painterResource(search)
    val image2 = painterResource(R.drawable.location)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp)) //裁剪成圆角矩形
            .background(Color.White)
            .border(1.dp, Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp), //横向左右俩边12.dp
        verticalAlignment = Alignment.CenterVertically //垂直方向居中对齐
    ) {
        //左边的搜索部分
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = image, contentDescription = null)
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search job") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        //中间的分割线
        Box(
            modifier = Modifier
                .height(20.dp)
                .width(1.dp)
                .background(Color.Gray)
        )
        //右边的搜索地区部分
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = image2, contentDescription = null)
            TextField(
                value = location,
                onValueChange = onLocationChange,
                placeholder = { Text("Location") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
    }
}

//负责搜索区域的背景
@Composable
fun Header(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    locationText: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                //！！！！！！！！！！！！！顶部渐变！！！！！！！！！！！！
                //渐变背景（gradient）brush刷，horizontalGradient横向渐变
                brush = Brush.horizontalGradient(
                    //创建了一个列表list
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )

//系统会自己在顶部加一个可以刚好避开状态栏的padding
            .statusBarsPadding()

    ) {

        Search(
            query = searchText,
            onQueryChange = onSearchChange,
            location = locationText,
            onLocationChange = onLocationChange,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}

//过滤器部分
@Composable
fun FilterBar(
    // 接收原本的 3 个按钮状态
    selectedChip: String, onChipClick: (String) -> Unit,
    // 接收所有的状态和修改事件
    selectedRecruiter: String, onRecruiterChange: (String) -> Unit,
    selectedJobType: String, onJobTypeChange: (String) -> Unit,
    selectedDate: String, onDateChange: (String) -> Unit,
    selectedSalary: String, onSalaryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val image = painterResource(R.drawable.filter) // 你的漏斗图标
    //我定义了一个叫expanded的变量，初始值是false关闭的，这一步是动画最开始的大脑部分
    var expanded by remember { mutableStateOf(false) } // 控制面板展开/收起

    Column(
        modifier = modifier
            .fillMaxWidth()
            //整体的大抽屉颜色，包括了隐藏的下拉部分
            .background(MaterialTheme.colorScheme.surface) // 使用表面颜色
            //魔法参数
            .animateContentSize(
                //alignment = Alignment.BottomStart
            ) //没有这行，下面面板就会突然出现，没有弹簧效果
            .padding(bottom = if (expanded) 16.dp else 0.dp) // 展开时底部留点白
    ) {
        //  第一行：漏斗图标 + 你原本的三个按钮 (保持不变)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 只有点这个图片才会触发下拉动画
            Image(
                painter = image,
                contentDescription = "Toggle Filters",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    //这个点击效果的作用是把expanded的效果反转，最开始咱们的expanded
                    //是关闭状态，你点击之后变成了true，接下来，就到展开后的高级选项面板
                    .clickable { expanded = !expanded }
                    .padding(4.dp)
            )

            // 原来的三个按钮
            FilterChip(
                text = "Date Posted",
                isSelected = selectedChip == "Date Posted",
                onClick = { onChipClick("Date Posted") }
            )
            FilterChip(
                text = "Job types",
                isSelected = selectedChip == "Job types",
                onClick = { onChipClick("Job types") }
            )
            FilterChip(
                text = "Salary",
                isSelected = selectedChip == "Salary",
                onClick = { onChipClick("Salary")}
            )
        }

        //展开后的高级选项面板，如果上边click让expanded状态变为true
        //那就会显示下边的东西
        if (expanded) {
            //这是一根分割线
            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // 1. 招聘人员 (Recruiters)
                FilterSectionTitle("Recruiters")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        "Direct Employer",
                        selectedRecruiter == "Direct",
                        { onRecruiterChange(if (selectedRecruiter == "Direct") "" else "Direct") })
                    FilterChip(
                        "Agency",
                        selectedRecruiter == "Agency",
                        { onRecruiterChange(if (selectedRecruiter == "Agency") "" else "Agency") })
                }

                // 2. 工作种类 (Job Types)
                FilterSectionTitle("Job Types")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        "Full-time",
                        selectedJobType == "Full-time",
                        { onJobTypeChange(if (selectedJobType == "Full-time") "" else "Full-time") })
                    FilterChip(
                        "Remote",
                        selectedJobType == "Remote",
                        { onJobTypeChange(if (selectedJobType == "Remote") "" else "Remote") })
                }

                // 3. 发布时间 (Date Posted)
                FilterSectionTitle("Date Posted")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        "Last 24 Hours",
                        selectedDate == "Hours",
                        { onDateChange(if (selectedDate == "Hours") "" else "Hours") })
                    FilterChip(
                        "Last Few Days",
                        selectedDate == "Days",
                        { onDateChange(if (selectedDate == "Days") "" else "Days") })
                }

                // 4. 期望薪资 (Salary) - 简化为预设区间，这比输入框好写且用户体验好
                FilterSectionTitle("Minimum Salary")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        "10K+",
                        selectedSalary == "10K",
                        { onSalaryChange(if (selectedSalary == "10K") "" else "10K") })
                    FilterChip(
                        "20K+",
                        selectedSalary == "20K",
                        { onSalaryChange(if (selectedSalary == "20K") "" else "20K") })
                }
            }
        }
    }
}

//过滤器的文本部分
@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            //创建盒子然后由text撑开盒子大小
            .clip(RoundedCornerShape(20.dp))
            .background(
                //这是过滤器按钮的颜色，未选中和选中的颜色有变化
                if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer  //选中时 主题容器色
                } else {
                    MaterialTheme.colorScheme.surfaceVariant  //未选中时候的表面变体色
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text)
    }
}

// 小助手：用来画过滤面板里的小标题
@Composable
fun FilterSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        //抽屉里的每个类别小标题的颜色！！！！
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
    )
}