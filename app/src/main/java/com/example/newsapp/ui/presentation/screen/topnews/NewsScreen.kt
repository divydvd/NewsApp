package com.example.newsapp.ui.presentation.screen.topnews

import android.graphics.Typeface.BOLD
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.R
import com.example.newsapp.domain.model.Article
import com.example.newsapp.ui.presentation.screen.article.ArticleScreen
import com.example.newsapp.ui.presentation.screen.destinations.ArticleScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
@Destination(start = true)
fun NewsScreen(
    navigator: DestinationsNavigator,
    newsViewModel: NewsViewModel = hiltViewModel(),
) {
    val state by newsViewModel.getState.collectAsState()
    val savedArticles by newsViewModel.savedArticles.observeAsState(initial = emptyList())

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    Log.d("helooo", state.savedArticles.toString())

    Box(modifier = Modifier) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = swipeRefreshState,
            onRefresh = {
                newsViewModel.onEvent(NewsListingEvent.Refresh)
            }
        ) {
            LazyColumn() {

                /**
                 * state.newsList.isEmpty() is to see if the response from the api has come through or nor
                 * !state.isLoading check to show the data from the database- i.e. don't show when the screen is loading
                 */
                if(state.newsList.isEmpty() && !state.isLoading) {
                    items(savedArticles) { article ->
                        val saved = savedArticles.contains(article)
                        NewsItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            article = article,
                            saved = saved,
                            navigateToArticle = {
                                navigator.navigate(
                                    ArticleScreenDestination(
                                        article
                                    )
                                )
                            },
                            onSaveIconClicked = { saveArticle ->
                                newsViewModel.saveArticleClicked(saveArticle)
                            }
                        )
                        Divider(color = colorResource(R.color.black))
                    }
                }
                else {
                    items(state.newsList) { article ->
                        val saved = savedArticles.contains(article)
                        NewsItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            article = article,
                            saved = saved,
                            navigateToArticle = {
                                navigator.navigate(
                                    ArticleScreenDestination(
                                        article
                                    )
                                )
                            },
                            onSaveIconClicked = { article ->
                                newsViewModel.saveArticleClicked(article)
                            }
                        )
                        Divider(color = colorResource(R.color.black))

                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    modifier: Modifier = Modifier,
    article: Article,
    saved: Boolean,
    navigateToArticle: () -> Unit,
    onSaveIconClicked: (Article) -> Unit,
) {

    Card(
        modifier = Modifier
            .background(Color.LightGray),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                )
                .wrapContentHeight()
                .fillMaxSize()
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterVertically)
                    .height(60.dp)
                    .width(40.dp),
                painter = rememberAsyncImagePainter(article.urlToImage),
                contentDescription = "News Image",
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterVertically)
                    .wrapContentHeight()
                    .clickable {
                        navigateToArticle()
                    }
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight(),
                    text = article.title,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                )

                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        )
                        .wrapContentHeight()
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .align(Alignment.CenterVertically),
                        text = article.author,
                        fontSize = 14.sp,
                    )

                    Spacer(modifier = Modifier.width(100.dp))

                    Icon(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                onSaveIconClicked(article)
                            },
                        contentDescription = "bookmark_icon",
                        painter = painterResource(
                            id = if (saved) R.drawable.baseline_bookmark_24
                            else R.drawable.baseline_bookmark_border_24
                        ),
                    )
                }
            }

        }
    }
}