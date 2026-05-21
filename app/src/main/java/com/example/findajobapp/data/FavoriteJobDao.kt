package com.example.findajobapp.data // 这里的包名根据你实际建的为准

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// @Dao 告诉 Room，这个接口就是专门负责干活的管家
@Dao
interface FavoriteJobDao {

    // 1. 收藏职位：插入一条数据。OnConflictStrategy.REPLACE 意思是如果碰巧存了两个一模一样的，就替换掉旧的
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(job: FavoriteJob)

    // 2. 取消收藏：删除这条数据（这是 Lab 5 里的 Optional 加分项哦！）
    @Delete
    suspend fun deleteFavorite(job: FavoriteJob)

    // 3. （查询）实时获取所有收藏：返回咱们刚才说的“数据水管” Flow
    @Query("SELECT * FROM favorite_jobs")
    fun getAllFavorites(): Flow<List<FavoriteJob>>
}