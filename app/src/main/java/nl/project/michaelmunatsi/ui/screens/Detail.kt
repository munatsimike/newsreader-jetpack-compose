package nl.project.michaelmunatsi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.ui.ImageViewer
import nl.project.michaelmunatsi.ui.layouts.LikeDisLikeArticle
import nl.project.michaelmunatsi.ui.theme.Transparent
import nl.project.michaelmunatsi.utils.MyUtility.UrlLinkBuilder
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

object Detail {
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        onBackBtnClick: () -> Unit = {},
        articleId: Int,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        sharedUserViewModel: UserViewModel
    ) {
        val newsArticle = sharedNewsViewModel.selectedArticle
        if (newsArticle != null && newsArticle.Id == articleId) {
            Surface(
                modifier = modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = modifier.verticalScroll(rememberScrollState())
                ) {
                    Box {
                        ImageViewer(imageUrl = newsArticle.Image, size = 380)
                        Box(
                            modifier = modifier
                                .padding(dimen.dp_20)
                                .clip(CircleShape)
                                .size(40.dp)
                        ) {
                            IconButton(
                                modifier = modifier.background(Transparent),
                                onClick = onBackBtnClick
                            ) {
                                Icon(
                                    modifier = modifier.size(dimen.dp_40),
                                    imageVector = Icons.Default.ArrowBack,
                                    tint = MaterialTheme.colors.secondary,
                                    contentDescription = resource.getString(R.string.back_button),
                                )
                            }
                        }
                    }
                    Column(modifier = modifier.padding(dimen.dp_15)) {
                        Text(text = newsArticle.Summary)
                        Spacer(modifier = modifier.height(dimen.dp_15))
                        UrlLinkBuilder(url = newsArticle.Url)
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (newsArticle.Related.isNotEmpty()) Text(
                                text = resource.getString(R.string.related),
                                fontSize = dimen.sp_20,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = modifier.weight(1f))
                            LikeDisLikeArticle.Layout(
                                isChecked = newsArticle.IsLiked,
                                scaffoldState = scaffoldState,
                                userViewModel = sharedUserViewModel,

                                ) {
                                sharedNewsViewModel.likeDislike(
                                    articleId = newsArticle.Id,
                                    !newsArticle.IsLiked
                                )
                            }
                        }
                        for (index in newsArticle.Related.indices) {
                            UrlLinkBuilder(
                                index = (index + 1).toString(),
                                url = newsArticle.Related[index]
                            )
                        }
                    }
                }
            }
        }
    }
}

