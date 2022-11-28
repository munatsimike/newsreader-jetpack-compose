package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.ui.ImageViewer
import nl.project.michaelmunatsi.ui.layouts.LikeDisLikeArticle
import nl.project.michaelmunatsi.ui.theme.Transparent
import nl.project.michaelmunatsi.utils.MyUtility.UrlLinkBuilder
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.utils.MyUtility.formatDate
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.utils.MyUtility.shareSheetIntent
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

// code for the detail screen, this screen will use articleId argument to fetch selected article from room
object Detail {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        onBackBtnClick: () -> Unit,
        articleId: Int,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        sharedUserViewModel: UserViewModel,
    ) {

        //fetch selected item from room
        LaunchedEffect(Unit) {
            sharedNewsViewModel.fetchArticleById(articleId)
        }

        // userstate can be logged in or logged out.
        // Depending on the userstate a user can like or dislike the displayed article
        val userState by sharedUserViewModel.userState.collectAsState()
        // collect the fetched article
        val newsArticle by sharedNewsViewModel.article.collectAsState()

        // Display article if collected article is the same as the one selected by the user.
        newsArticle?.let { article ->
            val context = LocalContext.current
            val sheetIntent = shareSheetIntent(text = article.Url)
            Surface(
                modifier = modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = modifier.verticalScroll(rememberScrollState())
                ) {
                    // display article image
                    Box {
                        ImageViewer(
                            imageUrl = article.Image, size = 380
                        )
                        // display back arrow on top of the image
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
                    // display category
                    Column(modifier = modifier.padding(dimen.dp_15)) {
                        Row {
                            article.Categories.forEach {
                                Text(
                                    text = it.Name,
                                    style = MaterialTheme.typography.subtitle1,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }
                        // display article date published
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatDate(article.PublishDate),
                                style = MaterialTheme.typography.subtitle1
                            )

                            Spacer(modifier = modifier.weight(1f))
                            // display share icon
                            IconButton(modifier = modifier.padding(start = 15.dp, end = 15.dp),
                                onClick = { context.startActivity(sheetIntent) }) {
                                Icon(
                                    modifier = modifier.size(28.dp),
                                    imageVector = Icons.Default.Share,
                                    contentDescription = resource.getString(R.string.share_icon),
                                    tint = MaterialTheme.colors.secondary
                                )

                            }
                            // show like and dislike button
                            LikeDisLikeArticle.Layout(
                                isChecked = article.IsLiked,
                                scaffoldState = scaffoldState,
                                userState = userState,

                                ) {
                                sharedNewsViewModel.likeDislike(
                                    articleId = articleId, !article.IsLiked
                                )
                            }
                        }

                        // show article summary
                        Text(
                            text = article.Summary, style = MaterialTheme.typography.body1
                        )
                        // show  a article Url
                        Spacer(modifier = modifier.height(dimen.dp_15))
                        UrlLinkBuilder(url = article.Url)
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // check if article contains related articles
                            if (article.Related.isNotEmpty()) Text(
                                text = resource.getString(R.string.related),
                                style = MaterialTheme.typography.body1,
                            )
                            Spacer(modifier = modifier.weight(1f))
                        }
                        // show related articles
                        for (index in article.Related.indices) {
                            UrlLinkBuilder(
                                index = (index + 1).toString(), url = article.Related[index]
                            )
                        }
                    }
                }
            }
        }
    }
}

