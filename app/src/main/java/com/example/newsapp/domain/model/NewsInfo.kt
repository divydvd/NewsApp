package com.example.newsapp.domain.model

data class NewsInfo(
    val articles: List<Article>,
    val isSaved: Boolean = false
)

data class Article(
    val title: String,
    val source: Source,
    val publishedAt: String,
    val urlToImage: String,
    val url: String
)

data class Source(
    val name: String
)
