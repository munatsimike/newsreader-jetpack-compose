package nl.project.michaelmunatsi.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.utils.MyUtility.formatDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsArticleLayout(
    modifier: Modifier = Modifier,
    article: NewsArticle,
    onMainClick: (route: String) -> Unit
) {
    Card(
        modifier = modifier
            .paddingFromBaseline(bottom = dimen.dp_40)
            .clip(RoundedCornerShape(dimen.dp_10)),
        elevation = 8.dp,

    ) {
        Column(
            modifier = modifier.padding(dimen.dp_15)
        ) {
            Row {
                // show article image
                ImageViewer(imageUrl = article.Image, size = 110, cornerRadius = 10)

                Spacer(modifier = modifier.width(dimen.dp_15))
                // display article title
                Column() {
                    Text(
                        text = article.Title,
                        fontWeight = FontWeight.Bold,
                    )

                    // display date
                    Row {
                        Text(
                            text = formatDate(article.PublishDate),
                            modifier = modifier.padding(dimen.dp_15)
                        )
                        Spacer(modifier = modifier.weight(1f))
                        // like or dislike and article
                        LikeDislikeIcon()
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