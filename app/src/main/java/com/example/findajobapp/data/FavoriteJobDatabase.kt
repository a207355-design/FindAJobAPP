package com.example.findajobapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// :: 这个符号叫类引用 favoritejob是一个类名，但是@database编译器不知道什么是类名，所以这个符号作用是相当于转换一下
//并且实体后面的版本和exportSchema也是必须要有的，版本是你修改的时候就需要把1改成2这样子，后面那个是告诉数据库要不要把JSON格式导出来
@Database(entities = [FavoriteJob::class, ChatMessage::class], version = 1, exportSchema = false)
abstract class FavoriteJobDatabase : RoomDatabase() {

    abstract fun favoriteJobDao(): FavoriteJobDao
    abstract fun chatDao(): ChatDao

    //companion 伴生，它的作用是把object（对象，正常写一个类的时候你可以new无数个A，但是有了object，你就只能有一个A）
    //把object和companion所在的这个类绑定在一起
    companion object {

        //@Volatile相当于告诉CPU不要使用缓存的数据，要去找咱们最开始的数值
        @Volatile
        //私有的 可变的变量instance     然后这个？告诉了系统这个变量可能存着数据库可能啥都没有
        //null意思就是在没有数据库前，它是个空盒子
        private var Instance: FavoriteJobDatabase? = null

        //(context: Context)像一张身份证，后面是你返回类型
        fun getDatabase(context: Context): FavoriteJobDatabase {
            //?:如果instance不是空就把它的值给我，如果是空的执行后面的代码
            //synchronized同步锁 ,锁的this这里指的就是FavoriteJobDatabase
            return Instance ?: synchronized(this) {
                //room官方提供的创建数据库的方法
                Room.databaseBuilder(
                    context, //应用上下文，告诉room去哪里创建数据库文件
                    FavoriteJobDatabase::class.java, //数据库类本身，我定义的自roomdatabase的类
                    "job_database" //数据库文件名
                )
                    //当你修改了数据库结构的时候，但没写迁移方案，room默认会崩溃，加上这个room就不会崩溃了
                    //而是删除旧数据库，重新创建一个新的数据库
                    .fallbackToDestructiveMigration()
                    .build() //上边的只是先返回一个构造器，这里才是真正的开始创建数据库
                    .also { Instance = it } //会对当前对象做一些附加操作
            }
        }
    }
}