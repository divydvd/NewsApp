package com.example.newsapp.ui.presentation.screen.article

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.domain.model.Article
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun ArticleScreen(
    article: Article,
) {
    Scaffold(
        modifier = Modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                modifier = Modifier
                    .fillMaxSize(.5f)
                    .align(Alignment.CenterHorizontally),
                painter = rememberAsyncImagePainter(article.urlToImage),
                contentDescription = "Article Image",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column() {

                Text(
                    modifier = Modifier,
                    text = article.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    text = article.content
                )
            }


//            Spacer(modifier = Modifier.height(8.dp))
//
//        Row(modifier = Modifier) {
//            Text(text = article.author)
//            Spacer(modifier = Modifier.width(8.dp))
//
//        }
            Log.d("ArticleScreen", article.content)
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                text = article.content
            )

        }
    }

}