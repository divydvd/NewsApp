package com.example.newsapp.data.remote.dto

import com.squareup.moshi.Json

data class NewsResponseDto(
    @field:Json(name = "articles")
    val articles: List<Article>?,

    @field:Json(name = "status")
    val status: String?,

    @field:Json(name = "totalResults")
    val totalResults: Int?
)

data class Article(
    @field:Json(name = "author")
    val author: String?,

    @field:Json(name = "content")
    val content: String?,

    @field:Json(name = "description")
    val description: String?,

    @field:Json(name = "publishedAt")
    val publishedAt: String?,

    @field:Json(name = "source")
    val source: Source?,

    @field:Json(name = "title")
    val title: String?,

    @field:Json(name = "url")
    val url: String?,

    @field:Json(name = "urlToImage")
    val urlToImage: String?
)

data class Source(
    @field:Json(name = "id")
    val id: String?,

    @field:Json(name = "name")
    val name: String?
)