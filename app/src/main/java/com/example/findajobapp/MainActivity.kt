package com.example.findajobapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.findajobapp.ui.theme.FindAJobAPPTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //表示页面内容允许延申到状态栏下边
        setContent {
            FindAJobAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(

                        modifier = Modifier
                    )
                }
            }
        }
    }
    //今天下午的最新进度
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FindAJobAPPTheme {
        MainScreen()
    }
}
