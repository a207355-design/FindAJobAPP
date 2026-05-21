package com.example.findajobapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ⚠️ 修改 1：在 entities 数组里，加上 ChatMessage::class
@Database(entities = [FavoriteJob::class, ChatMessage::class], version = 1, exportSchema = false)
abstract class FavoriteJobDatabase : RoomDatabase() {

    abstract fun favoriteJobDao(): FavoriteJobDao

    // ⚠️ 修改 2：加上这一行，把 ChatDao 暴露出来！
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var Instance: FavoriteJobDatabase? = null

        fun getDatabase(context: Context): FavoriteJobDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FavoriteJobDatabase::class.java, "job_database")
                    .fallbackToDestructiveMigration() // 建议加上这一行，防止修改表结构后崩溃
                    .build()
                    .also { Instance = it }
            }
        }
    }
}