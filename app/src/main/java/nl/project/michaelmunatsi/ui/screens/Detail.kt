package nl.project.michaelmunatsi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.ui.ImageViewer
import nl.project.michaelmunatsi.ui.layouts.LikeDisLike
import nl.project.michaelmunatsi.ui.theme.Orange
import nl.project.michaelmunatsi.ui.theme.Transparent
import nl.project.michaelmunatsi.utils.MyUtility.UrlLinkBuilder
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

object Detail {
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        onBackBtnClick: () -> Unit = {},
        articleId: Int,
        viewModel: NewsViewModel = hiltViewModel(),
        scaffoldState: ScaffoldState,
        userViewModel: UserViewModel
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
                                .padding(dimen.dp_20)
                                .clip(CircleShape)
                                .size(dimen.dp_40)
                        )
                        {
                            IconButton(
                                modifier = modifier
                                    .background(Transparent),
                                onClick = onBackBtnClick
                            ) {
                                Icon(
                                    modifier = modifier.size(dimen.dp_40),
                                    imageVector = Icons.Default.ArrowBack,
                                    tint = Orange,
                                    contentDescription = "Back button",
                                )
                            }
                        }
                    }
                    Column(modifier = modifier.padding(dimen.dp_15)) {
                        Text(text = article.Summary)
                        Spacer(modifier = modifier.height(dimen.dp_15))
                        UrlLinkBuilder(url = article.Url)
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (article.Related.isNotEmpty()) Text(
                                text = "Related", fontSize = dimen.sp_20, fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = modifier.weight(1f))
                            LikeDisLike.Layout(isChecked = article.IsLiked, scaffoldState = scaffoldState, userViewModel = userViewModel)
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
