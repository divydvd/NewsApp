package com.example.newsapp.ui.presentation.screen.topnews

import com.example.newsapp.domain.model.Article

data class NewsListingState(
    val newsList: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSaved: Boolean = false,
    val savedArticles: List<Article> = emptyList()
)
