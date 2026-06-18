package com.example.findajobapp.data // 这里的包名根据你实际建的为准

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// @Dao 告诉 Room，这个接口就是专门负责干活的管家
@Dao
//interface部分就是负责管理你指定的那个数据库的操作的
interface FavoriteJobDao {

    // 1. 收藏职位：插入一条数据。OnConflictStrategy.REPLACE 意思是如果碰巧存了两个一模一样的，就替换掉旧的
    //onConflict意思是在遇到冲突的时候，OnConflictStrategy是room自带的一种方法
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend 是告诉Dao不要走主线程，用子线程做这样不会让屏幕进行卡顿甚至闪退
    //(job: FavoriteJob)这个部分就是对应你这个操作是要操作哪个里面的，这就是咱们前面创建的数据库
    suspend fun insertFavorite(job: FavoriteJob)


    // 2. 取消收藏：删除这条数据（这是 Lab 5 里的 Optional 加分项哦！）
    @Delete
    suspend fun deleteFavorite(job: FavoriteJob)

    // 3. （查询）实时获取所有收藏：返回咱们刚才说的“数据水管” Flow
    //*（星号）：代表“所有列”（id, title, company, salary... 我全都要，一个格子都别落下）。
    //FROM favorite_jobs：从哪找？从咱们第一步建的那张名叫 favorite_jobs 的表格里找。
    @Query("SELECT * FROM favorite_jobs")
    //List 就是“列表”、“一沓”。
    //一个 FavoriteJob 是一张纸；List<FavoriteJob> 就是一沓纸（把你收藏的 10 个职位装订成了一册）
    //flow就是水管，相当于让让里面静态数据变成动态的，不需要重新跑一遍流程了
    fun getAllFavorites(): Flow<List<FavoriteJob>>

//@Query("DELETE FROM favorite_jobs")
//suspend fun deleteAll()
}