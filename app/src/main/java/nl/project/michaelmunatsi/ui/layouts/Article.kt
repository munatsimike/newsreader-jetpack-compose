package nl.project.michaelmunatsi.ui.layouts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.utils.MyUtility.formatDate
import nl.project.michaelmunatsi.viewModel.NewsViewModel

// contains code for displaying an article: date, title, image and summary
object Article {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Layout(
        modifier: Modifier = Modifier,
        scaffoldState: ScaffoldState,
        userState: UserState,
        sharedViewModel: NewsViewModel,
        article: NewsArticle,
        onArticleTitleClick: () -> Unit,
    ) {
        Card(
            modifier = modifier
                .padding(bottom = 2.dp)
                .clip(RoundedCornerShape(0.dp)),
            elevation = 10.dp,

            ) {
            Column(
                modifier = modifier.padding(15.dp)
            ) {
                Row {
                    // show article image
                    ImageViewer.Layout(imageUrl = article.Image, size = 110, cornerRadius = 10)

                    Spacer(modifier = modifier.width(15.dp))
                    // display article title
                    Column {
                        TextButton(
                            onClick = {
                                onArticleTitleClick.invoke()
                            }
                        ) {
                            Text(
                                text = article.Title,
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.primaryVariant
                            )
                        }

                        // display date
                        Row {
                            Text(
                                text = formatDate(article.PublishDate),
                                modifier = modifier.padding(15.dp)
                            )
                            Spacer(modifier = modifier.weight(1f))
                            // like or dislike and article
                            LikeDisLikeArticle.Layout(
                                isChecked = article.IsLiked,
                                scaffoldState = scaffoldState,
                                userState = userState,
                            ) { sharedViewModel.likeDislike(article.Id, !article.IsLiked) }
                        }
                    }
                }
                // display news summary
                Column {
                    Text(
                        text = article.Summary, maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}