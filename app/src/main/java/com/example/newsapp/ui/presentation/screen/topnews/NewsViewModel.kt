package com.example.newsapp.ui.presentation.screen.topnews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    private val _state = MutableStateFlow(NewsListingState())
    val getState = _state.asStateFlow()

    private val _savedArticles = MutableStateFlow(emptyList<Article>())
    val getSavedArticles = _savedArticles.asStateFlow()

    init {
        fetchLatestNews()
        viewModelScope.launch {
            repository.fetchSavedArticles().collect {
                _savedArticles.value = it
            }
        }
    }

    fun onEvent(event: NewsListingEvent) {
        when(event) {
            is NewsListingEvent.Refresh -> {
                fetchLatestNews()
            }
            else -> {}
        }
    }

    private fun fetchLatestNews() {
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .fetchArticles()
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let { newsInfo ->
                                _state.value = NewsListingState(newsList = newsInfo.articles, isLoading = false, isSaved = false)
                            }
                        }
                        is Resource.Error -> {

                        }
//                        is Resource.Loading -> {
//                            _state.value.copy(
//                                isLoading = result.isLoading
//                            )
//                        }
                        else -> {

                        }
                    }
                }
        }
    }

}