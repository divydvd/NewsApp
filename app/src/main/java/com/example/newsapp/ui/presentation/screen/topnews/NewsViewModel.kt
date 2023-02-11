package com.example.newsapp.ui.presentation.screen.topnews

import android.util.Log
import androidx.lifecycle.*
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.Permission
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewsListingState())
    val getState = _state.asStateFlow()

    private val _savedArticles = repository.fetchSavedArticles().asLiveData()
    val savedArticles: LiveData<List<Article>>
        get() = _savedArticles

    init {
        fetchLatestNews()
    }

    fun onEvent(event: NewsListingEvent) {
        when (event) {
            is NewsListingEvent.Refresh -> {
                fetchLatestNews()
            }
            else -> {}
        }
    }

    fun saveArticleClicked(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_savedArticles.value?.contains(article) == true) {
                repository.deleteSavedArticle(article = article)
            } else {
                repository.insertSavedArticles(article = article)
            }
        }
    }

    private fun fetchLatestNews() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository
                    .fetchArticles()
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { newsInfo ->
                                    _state.value = NewsListingState(
                                        newsList = newsInfo.articles,
                                        isLoading = false,
                                        isSaved = false
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                _state.value = NewsListingState(
                                    newsList = mutableListOf(),
                                    isLoading = true,
                                    isSaved = false
                                )
                            }
                            is Resource.Error -> {
                                _state.value = NewsListingState(
                                    newsList = emptyList(),
                                    isLoading = false,
                                    isSaved = false
                                )
                            }
                        }
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}