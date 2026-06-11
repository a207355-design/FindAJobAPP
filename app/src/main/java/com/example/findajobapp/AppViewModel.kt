package com.example.findajobapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findajobapp.data.ChatMessage
import com.example.findajobapp.data.FavoriteJob
import com.example.findajobapp.data.FavoriteJobRepository
import com.example.findajobapp.data.NewsArticle
import com.example.findajobapp.data.RetrofitClient
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


data class CommunityPost(
    val author: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class UserProfile(
    val name: String = "SUN HUAYI",
    val gender: String = "Male",
    val age: String = "21",
    val location: String = "Saville",
    val email: String = "sun@example.com",
    val phone: String = "+60 123456789"
)

class AppViewModel(private val repository: FavoriteJobRepository) : ViewModel() {

    // =========================================================
    // 1. 搜索与筛选流 (保持不变)
    // =========================================================
    var searchText = mutableStateOf("")
        private set
    var locationText = mutableStateOf("")
        private set

    fun updateSearch(text: String) { searchText.value = text }
    fun updateLocation(text: String) { locationText.value = text }

    val favoriteJobs: StateFlow<List<FavoriteJob>> = repository.getAllFavoritesStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val activeMessages: StateFlow<List<ChatMessage>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun isFavorite(job: Job): Boolean {
        return favoriteJobs.value.any { it.title == job.title && it.company == job.company }
    }

    fun toggleFavorite(job: Job) {
        viewModelScope.launch {
            if (isFavorite(job)) {
                val favJob = favoriteJobs.value.first { it.title == job.title && it.company == job.company }
                repository.deleteFavorite(favJob)
            } else {
                repository.insertFavorite(FavoriteJob(
                    title = job.title, company = job.company,
                    location = job.location, time = job.time, salary = job.salary
                ))
                repository.insertMessage(ChatMessage(
                    hrName = "${job.company} HR",
                    jobTitle = job.title,
                    content = "Hi, I noticed you are interested in the ${job.title} position. Are you available for a chat?"
                ))
            }
        }
    }

    // =========================================================
    // 2. 个人资料模块 (保持不变)
    // =========================================================
    var profile = mutableStateOf(UserProfile())
        private set

    fun updateName(newName: String) { profile.value = profile.value.copy(name = newName) }
    fun updateGender(newGender: String) { profile.value = profile.value.copy(gender = newGender) }
    fun updateAge(newAge: String) { profile.value = profile.value.copy(age = newAge) }
    fun updateProfileLocation(newLocation: String) { profile.value = profile.value.copy(location = newLocation) }
    fun updateEmail(newEmail: String) { profile.value = profile.value.copy(email = newEmail) }
    fun updatePhone(newPhone: String) { profile.value = profile.value.copy(phone = newPhone) }

    // =========================================================
    // 3. 聊天详情模块 (保持不变)
    // =========================================================
    var chatMessages = androidx.compose.runtime.mutableStateListOf<String>("Hi, are you available for interview?")
        private set

    fun sendMessage(msg: String) {
        if (msg.isNotBlank()) {
            chatMessages.add(msg)
        }
    }

    // =========================================================
    // 4. [新增] 外部 API 模块 (获取实时公司新闻)
    // =========================================================
    var companyNews = mutableStateListOf<NewsArticle>()
        private set

    var isNewsLoading = mutableStateOf(false)
        private set

    var newsErrorMessage = mutableStateOf("")
        private set

    fun fetchCompanyNews(companyName: String) {
        viewModelScope.launch {
            isNewsLoading.value = true
            newsErrorMessage.value = ""
            companyNews.clear()

            try {
                // 如果 API Key 没填或者是空的，直接不发请求，防止服务器报 401 导致崩溃
                val apiKey = "e548445800ec4b84b2f3f227866a8fde" // 务必替换！
                if (apiKey == "YOUR_API_KEY_HERE" || apiKey.isEmpty()) {
                    newsErrorMessage.value = "Please enter the API Key in NewsApi.kt"
                    return@launch
                }

                val response = RetrofitClient.newsApiService.getCompanyNews(companyName, apiKey = apiKey)

                if (response.articles != null && response.articles.isNotEmpty()) {
                    companyNews.addAll(response.articles)
                } else {
                    newsErrorMessage.value = "No news found for $companyName"
                }
            } catch (e: Exception) {
                // 关键：捕捉所有网络异常，并把错误信息显示在界面上，而不是让 App 闪退
                newsErrorMessage.value = "Network error: ${e.localizedMessage}"
            } finally {
                isNewsLoading.value = false
            }
        }
    }

    // =========================================================
    // 5. [新增] Firebase 社区模块
    // =========================================================
    private val db = FirebaseFirestore.getInstance()
    var communityPosts = androidx.compose.runtime.mutableStateListOf<CommunityPost>()
        private set

    // 实时监听数据库中的帖子
    fun fetchCommunityPosts() {
        db.collection("community_posts")
            .orderBy("timestamp", Query.Direction.DESCENDING) // 按时间倒序，最新的在上面
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    communityPosts.clear()
                    for (doc in snapshot.documents) {
                        val post = doc.toObject(CommunityPost::class.java)
                        if (post != null) {
                            communityPosts.add(post)
                        }
                    }
                }
            }
    }

    // 发送新帖子到数据库
    fun addCommunityPost(content: String) {
        if (content.isNotBlank()) {
            val post = CommunityPost(
                author = profile.value.name, // 直接用你个人资料里的名字
                content = content,
                timestamp = System.currentTimeMillis()
            )
            db.collection("community_posts").add(post)
        }
    }
}