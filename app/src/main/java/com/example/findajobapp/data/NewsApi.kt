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
    @GET("v2/everything")
    suspend fun getCompanyNews(
        @Query("q") companyName: String,
        // 注意：这里需要填你自己的 API Key！去 https://newsapi.org/ 免费注册一个
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int = 3,
        @Query("language") language: String = "en"
    ): NewsResponse
}

// 3. Retrofit 实例生成器
object RetrofitClient {
    private const val BASE_URL = "https://newsapi.org/"

    val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}