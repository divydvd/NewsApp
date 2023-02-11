package com.example.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    val author: String,
    val title: String,
    val sourceName: String,
    val publishedAt: String,
    val urlToImage: String,
    val content: String,
    @PrimaryKey val url: String = ""
)

data class Source(
    val name: String
)

