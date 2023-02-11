package com.example.newsapp.ui.presentation.screen.topnews

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import java.security.Permission
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewsListingState())
    val getState = _state.asStateFlow()

    private val _savedArticles = MutableLiveData<List<Article>>()
    val getSavedArticles: LiveData<List<Article>>
        get() = _savedArticles

    init {
        fetchSavedArticles()
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
            if(_savedArticles.value?.contains(article) == true) {
                _savedArticles.postValue(_savedArticles.value?.minus(article))
                repository.deleteSavedArticle(article = article)
            }
            else {
                _savedArticles.postValue(_savedArticles.value?.plus(article) ?: listOf(article))
                repository.insertSavedArticles(article = article)
            }
        }
    }

    private fun fetchSavedArticles() {
        viewModelScope.launch {
            repository
                .fetchSavedArticles()
                .collect { savedArticles ->
                    _savedArticles.postValue(savedArticles)
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
                                    newsList = _savedArticles.value ?: emptyList(),
                                    isLoading = false,
                                    isSaved = false
                                )
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
        catch(e: Exception) {
            e.printStackTrace()
        }
    }

}