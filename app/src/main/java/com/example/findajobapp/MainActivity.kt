package com.example.findajobapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.findajobapp.ui.theme.FindAJobAPPTheme

class MainActivity : ComponentActivity() {

    // 1. 定义权限请求处理器
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        // 权限结果处理：如果有需要，可以在这里根据结果更新 UI 状态
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. 启动时自动触发权限请求弹窗
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        setContent {
            FindAJobAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 传入 innerPadding，确保页面布局正常
                    MainScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}