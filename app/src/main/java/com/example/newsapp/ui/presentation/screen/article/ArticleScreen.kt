package com.example.newsapp.ui.presentation.screen.article

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Source
import com.example.newsapp.ui.presentation.screen.topnews.NewsItem
import com.example.newsapp.util.timeFormatter
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun ArticleScreen(
    article: Article,
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Image(
                modifier = Modifier
                    .padding(5.dp)
                    .aspectRatio(ratio = 1f)
                    .border(2.dp, Color.Gray),
                painter = rememberAsyncImagePainter(article.urlToImage),
                contentDescription = "Article Image",
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
            )

            Box(modifier = Modifier) {
                Column(modifier = Modifier) {
                    Text(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        text = article.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    if(article.author.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .wrapContentHeight(), //${if(article.publishedAt.isNotEmpty()) timeFormatter(article.publishedAt) else ""}
                            text = "Reported by: ${article.author}",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        text = article.content,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }


        }
    }

}

@Preview
@Composable
fun ArticleScreenPreview() {

    val article = Article(
        author = "Divy",
        title = "Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups.",
        source = Source(name = "R4 Daily"),
        publishedAt = "10th Feb., 2023",
        urlToImage = "https://images.indianexpress.com/2023/02/Lithium.jpg",
        url = "",
        content = "Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups.Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups.Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups.Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups."
    )

    ArticleScreen(article = article)
}