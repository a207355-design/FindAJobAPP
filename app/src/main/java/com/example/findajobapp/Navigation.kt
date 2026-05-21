package com.example.findajobapp

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.findajobapp.screens.ChatScreen
import com.example.findajobapp.screens.FavoriteScreen
import com.example.findajobapp.screens.FindScreen
import com.example.findajobapp.screens.MessageScreen
import com.example.findajobapp.screens.ProfileScreen


@Composable
fun Bottom(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val love = painterResource(R.drawable.love)
    val find = painterResource(R.drawable.find)
    val message = painterResource(R.drawable.message)
    val profile = painterResource(R.drawable.profile)
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Find
        //跳转页面在这里改！！！！！！！！！！
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                navController.navigate("find")
            }
        ) {
            Image(painter = find, contentDescription = null)
            Text("Find", fontWeight = FontWeight.Bold)
        }

        // Favorite
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                navController.navigate("favorite")
            }
        ) {
            Image(painter = love, contentDescription = null)
            Text("Favorite", fontWeight = FontWeight.Bold)
        }

        // Message
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                navController.navigate("message")
            }
        ) {
            Image(painter = message, contentDescription = null)
            Text("Message", fontWeight = FontWeight.Bold)
        }

        // Profile
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                navController.navigate("profile")
            }
        ) {
            Image(painter = profile, contentDescription = null)
            Text("Profile", fontWeight = FontWeight.Bold)
        }
    }
}


//导航中心 mainscreen
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    //创建navController 控制页面跳转
    val navController = rememberNavController()
    // 👇👇👇 从这里开始替换：我们要写一个工厂 (Factory) 来告诉系统怎么组装 ViewModel 👇👇👇
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: AppViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                // 1. 获取手机上下文，拉起数据库
                val database = com.example.findajobapp.data.FavoriteJobDatabase.getDatabase(context)
                // 2. 把数据库的 DAO 交给前台接待员 Repository
                // 注意括号里面，逗号后面补上了 database.chatDao()
                val repository = com.example.findajobapp.data.FavoriteJobRepository(
                    database.favoriteJobDao(),
                    database.chatDao()  // <-- 必须补上这一行！
                )
                // 3. 把 Repository 塞进 ViewModel 的大脑里
                @Suppress("UNCHECKED_CAST")
                return AppViewModel(repository) as T
            }
        }
    )
    Column(modifier = modifier.fillMaxSize()) {

        //中间区域（导航控制）
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            //定义所有页面
            NavHost(
                navController = navController,
                startDestination = "find"
            ) {

                composable("find") {
                    FindScreen(viewModel)
                }

                composable("favorite") {
                    FavoriteScreen(viewModel)
                }

                composable("message") {
                    MessageScreen(navController, viewModel = viewModel)
                }

                composable("profile") {
                    ProfileScreen(viewModel)
                }

                // 新增：带参数的聊天页路由
                // "{sender}" 就像一个占位符，你传什么，它就接什么
                composable("chat/{sender}") { backStackEntry ->
                    // 从路由里把那个名字“抠”出来
                    val senderName = backStackEntry.arguments?.getString("sender")

                    // 把名字传给 ChatScreen 页面显示
                    ChatScreen(
                        navController = navController,
                        viewModel = viewModel,
                        senderName = senderName
                    )
                }
            }
        }

        //分割线
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )

        // Bottom（记得传 navController！）
        Bottom(
            navController = navController,
            modifier = Modifier.padding(16.dp)
        )
    }
}