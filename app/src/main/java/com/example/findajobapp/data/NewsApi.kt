package com.example.findajobapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. 数据模型
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>?
)

data class NewsArticle(
    val title: String?,
    val description: String?,
    val url: String?
)

// 2. 接口定义
interface NewsApiService {
    //总部的某个部门，它和下边的总部地址结合起来就是https://newsapi.org/v2/everything
    @GET("v2/everything")
    suspend fun getCompanyNews(
        //如果公司名是google，那么就是https://newsapi.org/v2/everything?q=google
        @Query("q") companyName: String,
        // 注意：这里需要填你自己的 API Key！去 https://newsapi.org/ 免费注册一个
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int = 3, // 每页返回的文章数量
        @Query("language") language: String = "en" //再拼上这个，就是https://newsapi.org/v2/everything?q=google&apiKey=xxx
    ): NewsResponse
}

// 3. Retrofit 实例生成器
object RetrofitClient {
    //NewsAPI总部地址
    private const val BASE_URL = "https://newsapi.org/"

    val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}