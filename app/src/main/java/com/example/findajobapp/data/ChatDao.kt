package com.example.findajobapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    //存
    @Insert
    suspend fun insertMessage(message: ChatMessage)
    //查
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>> // 这样消息就能实时显示
}