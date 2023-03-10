package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsInfo
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface NewsRepository {

    fun fetchSavedArticles(): Flow<List<Article>>

    suspend fun insertSavedArticles(article: Article)

    suspend fun deleteSavedArticle(article: Article)

    suspend fun fetchArticles(): Flow<Resource<NewsInfo>>

}