package com.example.newsapp.data.repository

import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.mapper.toDataArticle
import com.example.newsapp.data.mapper.toDomainArticle
import com.example.newsapp.data.mapper.toNewsInfo
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsInfo
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val db: NewsDatabase,

): NewsRepository {
    val dao = db.dao
    override suspend fun fetchSavedArticles(): Flow<List<Article>> {
        return dao.getSavedArticles().map {
            it.map { article ->
                article.toDomainArticle()
            }
        }
    }

    override suspend fun insertSavedArticles(article: Article) {
        dao.insertArticle(article.toDataArticle())
    }

    override suspend fun deleteSavedArticle(article: Article) {
        dao.clearSavedArticle(article = article.toDataArticle())
    }

    override suspend fun fetchArticles(): Flow<Resource<NewsInfo>> {
        return flow {
            emit(Resource.Loading(true))
            val response = api.fetchTopHeadlines()
            if(response.isSuccessful) {
                emit(Resource.Success(
                    data = response.body()?.toNewsInfo()
                ))
            }
            else {
                emit(Resource.Error(
                    message = "f"
                ))
            }
//            response.body()
//            response.data?.let { data ->
//                emit(Resource.Success(
//                    data = data.toNewsInfo()
//                ))
//            }
//                ?: run {
//                    emit(Resource.Error(
//                        message = "fat gyi"
//                    ))
//                }
        }
    }
}