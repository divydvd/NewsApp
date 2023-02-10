package com.example.newsapp.ui.presentation.screen.topnews

sealed class NewsListingEvent {

    object Refresh : NewsListingEvent()
}