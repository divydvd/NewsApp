package com.example.newsapp.data.mapper

import com.example.newsapp.data.remote.dto.Article
import com.example.newsapp.data.remote.dto.NewsResponseDto
import com.example.newsapp.domain.model.NewsInfo
import com.example.newsapp.domain.model.Source

fun NewsResponseDto.toNewsInfo(): NewsInfo {
    return NewsInfo(
        articles = articles?.map {
            com.example.newsapp.domain.model.Article(
                title = it.title ?: "",
                source = Source(it.source?.name ?: ""),
                publishedAt = it.publishedAt ?: "",
                urlToImage = it.urlToImage ?: "",
                url = it.url ?: "",
                author = it.author ?: "",
                content = it.content ?: ""
            )
        } ?: emptyList(),
        isSaved = false
    )
}