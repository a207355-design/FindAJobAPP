package com.example.findajobapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

//消息
data class Message(
    val sender: String,
    val lastMessage: String,
    val time: String,
    val isUnread: Boolean
)

//创建一个专门用来“管理数据”的类
class AppViewModel : ViewModel() {
    //可观察的状态变量
    var searchText = mutableStateOf("")
        //如果没有这个,任何地方都可以改,有了这个你就只能在ViewModel里面改
        private set
    var locationText = mutableStateOf("")
        private set

    // ⭐ 收藏列表（核心） 存所有被收藏的job
    //注意这里咱们的初始状态是空的（）所以一定是false 白心开始
    var favoriteJobs = mutableStateListOf<Job>()
        private set

    //更新用户输入
    fun updateSearch(text: String) {
        //searchText.value就是你这个searchText上边监听你输入的值是 = text
        searchText.value = text
    }
    fun updateLocation(text: String) {
        locationText.value = text
    }


    //  添加 / 移除收藏
    fun toggleFavorite(job: Job) {
        //如果已经收藏了 就 取消
        if (favoriteJobs.contains(job)) {
            favoriteJobs.remove(job)
        } else {
            //没收藏就  添加
            favoriteJobs.add(job)

            // ==========================================
            //  神奇联动：自动生成一条 HR 消息！
            // ==========================================
            val hrName = "${job.company} HR"
            val autoReply =
                "Hi, I noticed you are interested in the ${job.title} position. Are you available for a chat?"

            // 创建一个新的 Message 对象并加到消息列表里
            val newMessage = Message(
                sender = hrName,
                lastMessage = autoReply,
                time = "Just now",
                isUnread = true // 默认显示红点
            )

            // 避免重复添加：如果这个 HR 已经给你发过消息了，就不再重复发
            val alreadyExists = activeMessages.any { it.sender == hrName }
            if (!alreadyExists) {
                activeMessages.add(newMessage) // 核心！ADD 数据到列表
            }
        }
    }

    //状态判断
    //判断这个job有没有被收藏 返回一个布尔值，如果收藏了就是true
    fun isFavorite(job: Job): Boolean {
        //又因为第一次加载页面的时候是false，所以这里第一次传递的是false值
        return favoriteJobs.contains(job)
    }

    //账号数据管理
    var name = mutableStateOf("SUN HUAYI")
        private set

    var gender = mutableStateOf("Male")
        private set

    var age = mutableStateOf("21")
        private set

    var location = mutableStateOf("Saville")
        private set

    var email = mutableStateOf("sun@example.com")
        private set

    var phone = mutableStateOf("+60 123456789")
        private set

    //更新函数
    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateGender(newGender: String) {
        gender.value = newGender
    }

    fun updateAge(newAge: String) {
        age.value = newAge
    }

    fun updateProfileLocation(newLocation: String) {
        location.value = newLocation
    }

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePhone(newPhone: String) {
        phone.value = newPhone
    }
    // ================= 聊天功能数据 =================
    // 存当前聊天窗口的所有消息记录
    // 添加这个动态列表
    // 它使用 mutableStateListOf，这意味着只要列表里多了数据，UI 会立刻自动刷新
    var activeMessages = mutableStateListOf<Message>()
        private set
    var chatMessages = mutableStateListOf<String>(
        "Hi, are you available for interview?", // 默认的第一条假消息
    )
        private set

    // 发送新消息（这就是满足 ADD 要求的核心！）
    fun sendMessage(msg: String) {
        if (msg.isNotBlank()) {
            chatMessages.add(msg) // 把你的话加进列表
        }
    }
    // ================= 消除小红点功能 =================
    fun markMessageAsRead(senderName: String) {
        // 1. 在当前的聊天列表中，找到名字匹配的那个 HR
        val index = activeMessages.indexOfFirst { it.sender == senderName }

        // 2. 如果找到了，就把这条消息的状态改成“已读”（isUnread = false）
        if (index != -1) {
            // 在 Compose 里，要让 UI 刷新，我们需要用 copy 替换掉原来的数据
            activeMessages[index] = activeMessages[index].copy(isUnread = false)
        }
    }
}

