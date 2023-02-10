package com.example.newsapp.data.mapper

import com.example.newsapp.data.local.Article
import com.example.newsapp.domain.model.Source

fun Article.toDomainArticle() : com.example.newsapp.domain.model.Article {
    return com.example.newsapp.domain.model.Article(
        title = title,
        source = Source(
            name = sourceName
        ),
        publishedAt = publishedAt,
        urlToImage = urlToImage,
        url = url,
        author = author
    )
}

fun com.example.newsapp.domain.model.Article.toDataArticle() : Article {
    return Article(
        author = author,
        title = title,
        sourceName = source.name,
        publishedAt = publishedAt,
        urlToImage = urlToImage,
        url = url
    )
}