package nl.project.michaelmunatsi.ui.layouts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.utils.MyUtility.formatDate
import nl.project.michaelmunatsi.viewModel.UserViewModel

object NewsArticle {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Layout(
        modifier: Modifier = Modifier,
        scaffoldState: ScaffoldState,
        userViewModel: UserViewModel,
        article: nl.project.michaelmunatsi.model.NewsArticle,
        onArticleTitleClick: () -> Unit
    ) {
        Card(
            modifier = modifier
                .paddingFromBaseline(bottom = 40.dp)
                .clip(RoundedCornerShape(10.dp)),
            elevation = 8.dp,
            backgroundColor = Color(0xFFE9E4E4)

        ) {
            Column(
                modifier = modifier.padding(15.dp)
            ) {
                Row {
                    // show article image
                    ImageViewer.Layout(imageUrl = article.Image, size = 110, cornerRadius = 10)

                    Spacer(modifier = modifier.width(15.dp))
                    // display article title
                    Column() {
                        TextButton(onClick = onArticleTitleClick) {
                            Text(

                                text = article.Title,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
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
                            LikeDisLike.Layout(isChecked = article.IsLiked, scaffoldState = scaffoldState, userViewModel = userViewModel)
                        }
                    }
                }
                // display news summary
                Column {
                    Text(
                        text = article.Summary, maxLines = 3
                    )
                }

            }
        }
    }
}