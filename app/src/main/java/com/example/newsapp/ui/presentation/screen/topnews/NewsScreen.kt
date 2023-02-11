package com.example.newsapp.ui.presentation.screen.topnews

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.R
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Source
import com.example.newsapp.ui.presentation.screen.destinations.ArticleScreenDestination
import com.example.newsapp.util.timeFormatter
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
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /**
                 * state.newsList.isEmpty() is to see if the response from the api has come through or nor
                 * !state.isLoading check to show the data from the database- i.e. don't show when the screen is loading
                 */
                if (state.newsList.isEmpty() && !state.isLoading) {
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
                                        article = article,
                                    )
                                )
                            },
                            onSaveIconClicked = { saveArticle ->
                                newsViewModel.saveArticleClicked(saveArticle)
                            }
                        )
                        Divider(color = colorResource(R.color.black))
                    }
                } else {
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
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(60.dp)
                    .aspectRatio(ratio = 1f)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                painter = rememberAsyncImagePainter(article.urlToImage),
                contentDescription = "News Image",
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(fraction = 0.9f)
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
                    maxLines = 2,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .wrapContentHeight(), //${if(article.publishedAt.isNotEmpty()) timeFormatter(article.publishedAt) else ""}
                    text = "${if(article.publishedAt.isNotEmpty()) timeFormatter(article.publishedAt) else ""}  ${article.author}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
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

@Preview
@Composable
fun NewsItemPreview() {
    val article = Article(
        author = "Divy",
        title = "Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups.",
        source = Source(name = "R4 Daily"),
        publishedAt = "2023-02-09T19:37:33.1548403Z",
        urlToImage = "https://images.indianexpress.com/2023/02/Lithium.jpg",
        url = "",
        content = ""
    )
    NewsItem(
        article = article,
        saved = false,
        navigateToArticle = {},
        onSaveIconClicked = {})
}