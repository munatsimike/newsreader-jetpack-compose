package nl.project.michaelmunatsi.ui.screens

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.ui.ImageViewer
import nl.project.michaelmunatsi.ui.LikeDislikeIcon
import nl.project.michaelmunatsi.ui.theme.Orange
import nl.project.michaelmunatsi.ui.theme.Transparent
import nl.project.michaelmunatsi.utils.MyUtility.UrlLinkBuilder
import nl.project.michaelmunatsi.viewModel.NewsViewModel

object Detail {
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        onBackBtnClick: () -> Unit = {},
        articleId: Int,
        viewModel: NewsViewModel = hiltViewModel()
    ) {
        var newsArticle: NewsArticle? by remember { mutableStateOf(null) }
        LaunchedEffect(articleId) {
            newsArticle = viewModel.getArticle(articleId)
        }

        if (newsArticle != null) {
            val article: NewsArticle = newsArticle as NewsArticle
            Surface(
                modifier = modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = modifier.verticalScroll(rememberScrollState())
                ) {
                    Box {
                        ImageViewer(imageUrl = article.Image, size = 380)
                        Box(
                            modifier = modifier
                                .padding(20.dp)
                                .clip(CircleShape)
                                .size(40.dp)
                        )
                        {
                            IconButton(
                                modifier = modifier
                                    .background(Transparent),
                                onClick = onBackBtnClick
                            ) {
                                Icon(
                                    modifier = modifier.size(40.dp),
                                    imageVector = Icons.Default.ArrowBack,
                                    tint = Orange,
                                    contentDescription = "Back button",
                                )
                            }
                        }
                    }
                    Column(modifier = modifier.padding(15.dp)) {
                        Text(text = article.Summary)
                        Spacer(modifier = modifier.height(15.dp))
                        UrlLinkBuilder(url = article.Url)
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (article.Related.isNotEmpty()) Text(
                                text = "Related", fontSize = 20.sp, fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = modifier.weight(1f))
                            LikeDislikeIcon()
                        }

                        article.Related.forEach { link ->
                            UrlLinkBuilder(url = link)
                        }
                    }
                }
            }
        }
    }
}
