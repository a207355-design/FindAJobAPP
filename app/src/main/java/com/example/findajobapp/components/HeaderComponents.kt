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
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalContext
import android.location.Geocoder
import com.google.android.gms.location.Priority
import java.util.Locale

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
// 过滤器部分
@Composable
fun FilterBar(
    // 接收原本的 3 个按钮状态
    selectedChip: String, onChipClick: (String) -> Unit,
    // 接收所有的状态和修改事件
    selectedRecruiter: String, onRecruiterChange: (String) -> Unit,
    selectedJobType: String, onJobTypeChange: (String) -> Unit,
    selectedDate: String, onDateChange: (String) -> Unit,
    selectedSalary: String, onSalaryChange: (String) -> Unit,
    onLocationChange: (String) -> Unit, // 新增：用于更新位置
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    //创建定位客户端
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val image = painterResource(R.drawable.filter)
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .animateContentSize()
            .padding(bottom = if (expanded) 16.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = image,
                contentDescription = "Toggle Filters",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { expanded = !expanded }
                    .padding(4.dp)
            )
            FilterChip("Date Posted", selectedChip == "Date Posted") { onChipClick("Date Posted") }
            FilterChip("Job types", selectedChip == "Job types") { onChipClick("Job types") }
            FilterChip("Salary", selectedChip == "Salary") { onChipClick("Salary") }
        }

        if (expanded) {
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // 1-4 原有筛选逻辑...
                FilterSectionTitle("Recruiters")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip("Direct", selectedRecruiter == "Direct") { onRecruiterChange(if (selectedRecruiter == "Direct") "" else "Direct") }
                    FilterChip("Agency", selectedRecruiter == "Agency") { onRecruiterChange(if (selectedRecruiter == "Agency") "" else "Agency") }
                }

                FilterSectionTitle("Job Types")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip("Full-time", selectedJobType == "Full-time") { onJobTypeChange(if (selectedJobType == "Full-time") "" else "Full-time") }
                    FilterChip("Remote", selectedJobType == "Remote") { onJobTypeChange(if (selectedJobType == "Remote") "" else "Remote") }
                }

                FilterSectionTitle("Date Posted")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip("24 Hours", selectedDate == "Hours") { onDateChange(if (selectedDate == "Hours") "" else "Hours") }
                    FilterChip("Few Days", selectedDate == "Days") { onDateChange(if (selectedDate == "Days") "" else "Days") }
                }

                FilterSectionTitle("Minimum Salary")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip("10K+", selectedSalary == "10K") { onSalaryChange(if (selectedSalary == "10K") "" else "10K") }
                    FilterChip("20K+", selectedSalary == "20K") { onSalaryChange(if (selectedSalary == "20K") "" else "20K") }
                }

                // 5. 新增：定位按钮
                FilterSectionTitle("Current Location")
                Button(
                    onClick = {
                        //查看用户是否允许定位
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        )
                            == PackageManager.PERMISSION_GRANTED //判断权限结果如果等于这个，进入下一步
                            ) {
                            //在同意的前提下进入这一步，获取位置
                            fusedLocationClient.getCurrentLocation(
                                Priority.PRIORITY_HIGH_ACCURACY,
                                null
                            ).addOnSuccessListener { loc ->

                                if (loc != null) {

                                    val geocoder =
                                        Geocoder(context, Locale.getDefault())

                                    val addresses =
                                        geocoder.getFromLocation(
                                            loc.latitude,
                                            loc.longitude,
                                            1
                                        )

                                    if (!addresses.isNullOrEmpty()) {

                                        val city =
                                            addresses[0].locality ?: ""

                                        val state =
                                            addresses[0].adminArea ?: ""

                                        onLocationChange("$city, $state")
                                    }
                                }
                             else {

                                    onLocationChange("Not Found")
                                }
                            }
                        } else {
                            //如果没有权限，跳转到这里
                            onLocationChange("No Permission")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Get Current Location")
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