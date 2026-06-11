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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.findajobapp.screens.ChatScreen
import com.example.findajobapp.screens.CommunityScreen
import com.example.findajobapp.screens.FavoriteScreen
import com.example.findajobapp.screens.FindScreen
import com.example.findajobapp.screens.MessageScreen
import com.example.findajobapp.screens.ProfileScreen
import com.example.findajobapp.screens.JobDetailScreen

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
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("find") }
        ) {
            Image(painter = find, contentDescription = null)
            Text("Find", fontWeight = FontWeight.Bold,fontSize = 10.sp)
        }

        // 占位 Community 按钮 (图标用了默认的地球图标)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("community") }
        ) {
            Image(
                painter = painterResource(R.drawable.community),
                contentDescription = null
            )
            Text("Community", fontWeight = FontWeight.Bold,fontSize = 10.sp)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("favorite") }
        ) {
            Image(painter = love, contentDescription = null)
            Text("Favorite", fontWeight = FontWeight.Bold,fontSize = 10.sp)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("message") }
        ) {
            Image(painter = message, contentDescription = null)
            Text("Message", fontWeight = FontWeight.Bold,fontSize = 10.sp)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("profile") }
        ) {
            Image(painter = profile, contentDescription = null)
            Text("Profile", fontWeight = FontWeight.Bold,fontSize = 10.sp)
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: AppViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val database = com.example.findajobapp.data.FavoriteJobDatabase.getDatabase(context)
                val repository = com.example.findajobapp.data.FavoriteJobRepository(
                    database.favoriteJobDao(),
                    database.chatDao()
                )
                @Suppress("UNCHECKED_CAST")
                return AppViewModel(repository) as T
            }
        }
    )

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            NavHost(
                navController = navController,
                startDestination = "find"
            ) {
                // 注意这里传了 navController 进去
                composable("find") { FindScreen(viewModel, navController) }
                composable("favorite") { FavoriteScreen(viewModel) }
                composable("message") { MessageScreen(navController, viewModel = viewModel) }
                composable("profile") { ProfileScreen(viewModel) }
                composable("chat/{sender}") { backStackEntry ->
                    val senderName = backStackEntry.arguments?.getString("sender")
                    ChatScreen(navController = navController, viewModel = viewModel, senderName = senderName)
                }

                composable("community") {
                    // 等待下一步创建 CommunityScreen
                    CommunityScreen(viewModel = viewModel)
                }

                // 详情页路由
                composable("jobDetail/{companyName}") { backStackEntry ->
                    val companyName = backStackEntry.arguments?.getString("companyName")
                    JobDetailScreen(navController = navController, viewModel = viewModel, companyName = companyName)
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray)
        )

        Bottom(
            navController = navController,
            modifier = Modifier.padding(16.dp)
        )
    }
}