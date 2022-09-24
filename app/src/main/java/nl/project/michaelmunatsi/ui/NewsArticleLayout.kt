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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsArticleLayout(
    modifier: Modifier = Modifier,
    article: NewsArticle,
    onMainClick: (route: String) -> Unit
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
                ImageViewer(imageUrl = article.Image, size = 110, cornerRadius = 10)

                Spacer(modifier = modifier.width(15.dp))
                // display article title
                Column() {
                    Text(
                        text = article.Title,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF512DA8)
                    )

                    // display date
                    Row {
                        Text(
                            text = formatDate(article.PublishDate),
                            modifier = modifier.padding(15.dp)
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