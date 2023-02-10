package com.example.newsapp.data.remote

import com.example.newsapp.data.remote.dto.NewsResponseDto
import com.example.newsapp.util.Resource
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET
    suspend fun fetchTopHeadlines(
        @Query("country") country: String = "in",
        @Query("pageSize") pageSize: Int = 10,
        @Query("apiKey") apiKey: String = API_KEY
    ): Resource<NewsResponseDto>


    companion object {
        const val API_KEY = "d558ad7091a74d58a5e456a4a8e67471"
        const val BASE_URL = "https://newsapi.org"

    }
}

