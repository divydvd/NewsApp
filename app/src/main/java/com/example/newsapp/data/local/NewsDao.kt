package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(
        article: Article
    )

    @Query("SELECT * FROM article")
    fun getSavedArticles(): Flow<List<Article>>

    @Delete
    fun clearSavedArticle(article: Article)
}