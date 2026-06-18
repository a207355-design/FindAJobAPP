package com.example.findajobapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity 告诉 Room 这是一张数据库表，表名叫 "favorite_jobs"
@Entity(tableName = "favorite_jobs")
data class FavoriteJob(
// @PrimaryKey 告诉 Room 每一行都需要一个独一无二的身份证号，autoGenerate = true 让它自己按顺序生成 1,2,3...
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val company: String,
    val location: String,
    val time: String,
    val salary: String
    //val 什么东西：String
)