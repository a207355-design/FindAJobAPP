package com.example.findajobapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findajobapp.data.ChatMessage
import com.example.findajobapp.data.FavoriteJob
import com.example.findajobapp.data.FavoriteJobRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 保持原有的 Profile 定义
data class UserProfile(
    val name: String = "SUN HUAYI",
    val gender: String = "Male",
    val age: String = "21",
    val location: String = "Saville",
    val email: String = "sun@example.com",
    val phone: String = "+60 123456789"
)

class AppViewModel(private val repository: FavoriteJobRepository) : ViewModel() {

    // 搜索与筛选 (保持不变)
    var searchText = mutableStateOf("")
        private set
    var locationText = mutableStateOf("")
        private set

    fun updateSearch(text: String) { searchText.value = text }
    fun updateLocation(text: String) { locationText.value = text }

    // 数据库实时流 (收藏职位)
    val favoriteJobs: StateFlow<List<FavoriteJob>> = repository.getAllFavoritesStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    // 数据库实时流 (持久化消息)
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
                // 持久化存储 HR 消息
                repository.insertMessage(ChatMessage(
                    hrName = "${job.company} HR",
                    jobTitle = job.title,
                    content = "Hi, I noticed you are interested in the ${job.title} position. Are you available for a chat?"
                ))
            }
        }
    }

    // =========================================================
    // 3. 个人资料模块 (保持不变)
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
    // 1对1 聊天详情页模块 (ChatScreen 专用)
    // =========================================================
    var chatMessages = androidx.compose.runtime.mutableStateListOf<String>("Hi, are you available for interview?")
        private set

    fun sendMessage(msg: String) {
        if (msg.isNotBlank()) {
            chatMessages.add(msg)
        }
    }
}