package com.example.newsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsInfo(
    val articles: List<Article> = emptyList(),
    val isSaved: Boolean = false
)

@Parcelize
data class Article(
    val author: String,
    val title: String,
    val source: Source,
    val publishedAt: String,
    val urlToImage: String,
    val url: String,
    val content: String,
) : Parcelable

@Parcelize
data class Source(
    val name: String
) : Parcelable
