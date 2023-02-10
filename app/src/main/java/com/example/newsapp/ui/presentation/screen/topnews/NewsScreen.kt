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
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
    val savedArticles by newsViewModel.getSavedArticles.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    Log.d("hello", state.toString())
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            newsViewModel.onEvent(NewsListingEvent.Refresh)
        }
    ) {
        LazyColumn() {
            items(state.newsList) { article ->
                NewsItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    article = article,
                    savedArticles = savedArticles,
                    navigateToArticle = {
                        navigator.navigate(ArticleScreenDestination())
                    },
                )
                Divider(color = colorResource(R.color.black))

            }
        }
    }
}

@Composable
fun NewsItem(
    modifier: Modifier = Modifier,
    article: Article,
    savedArticles: List<Article>,
    navigateToArticle: () -> Unit,
) {
    val saved = savedArticles.contains(article)
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
                    fontSize = 15.sp,
                )
                Text(
                    modifier = Modifier
                        .wrapContentHeight(),
                    text = article.author,
                    fontSize = 12.sp,
                )
            }

            Icon(
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = "bookmark_icon",
                painter = painterResource(
                    id = if (saved) R.drawable.baseline_bookmark_24
                    else R.drawable.baseline_bookmark_border_24
                ),
            )
        }
    }
}