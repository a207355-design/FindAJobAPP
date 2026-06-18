package com.example.findajobapp.data // 确保包名一致

import kotlinx.coroutines.flow.Flow

// 核心修改：在构造函数中注入 ChatDao
class FavoriteJobRepository(
    private val favoriteJobDao: FavoriteJobDao,
    private val chatDao: ChatDao
) {

    // ==========================================
    // 1. 职位收藏相关 (保持不变)
    // ==========================================
    fun getAllFavoritesStream(): Flow<List<FavoriteJob>> = favoriteJobDao.getAllFavorites()

    suspend fun insertFavorite(job: FavoriteJob) = favoriteJobDao.insertFavorite(job)

    suspend fun deleteFavorite(job: FavoriteJob) = favoriteJobDao.deleteFavorite(job)

    // ==========================================
    // 2. 聊天消息相关 (新增这部分)
    // ==========================================

    // 获取所有聊天消息的“实时水管”
    val allMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()

    // 存入一条新消息
    suspend fun insertMessage(message: ChatMessage) = chatDao.insertMessage(message)

    //suspend fun deleteAll() = favoriteJobDao.deleteAll()
}