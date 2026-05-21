package com.example.findajobapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hrName: String,
    val jobTitle: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)