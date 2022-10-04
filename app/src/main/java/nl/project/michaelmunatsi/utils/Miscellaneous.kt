package nl.project.michaelmunatsi.utils

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.NewsArticle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MyUtility {

    lateinit var resource: Resources
    lateinit var dimen: Dimensions

    @Composable
    fun InitResources() {
        resource = LocalContext.current.resources
        dimen = LocalDim.current
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(localDateTime: String): String {
        val date = LocalDateTime.parse(localDateTime)
        val pattern = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        return date.format(pattern)
    }

    @Composable
    fun shareSheetIntent(text: String): Intent? {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, null)
    }

    @Composable
    fun UrlLinkBuilder(
        modifier: Modifier = Modifier,
        url: String,
        index: String = "",
        text: String = url
    ) {
        val uriHandler = LocalUriHandler.current
        Row(modifier = modifier.padding(6.dp)) {
            Text(text = index)
            Spacer(modifier = modifier.width(6.dp))
            Text(
                modifier = modifier
                    .clickable {
                        uriHandler.openUri(url)
                    },

                text = text,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }

    fun destinationFromUrl(destinationUrl: String): String {
        val firstSlashIndex = destinationUrl.indexOf("/")
        return if (firstSlashIndex == -1) destinationUrl else destinationUrl.substring(
            0,
            firstSlashIndex
        )
    }

    // convert bool to int
    fun Boolean.toInt() = if (this) 1 else 0

    fun onFailureMessage() {
        throw IllegalStateException(resource.getString(R.string.No_internet_access))
    }

    fun onErrorMessage(statusCode: Int, message: String) {
        if (statusCode == 401) {
            throw IllegalStateException(resource.getString(R.string.user_not_logged_in))
        } else {
            throw IllegalStateException(message)
        }
    }

    // validate API response using news article mapper
    fun getMapperResult(result: Result<List<NewsArticle>>): List<NewsArticle> {
        val mapperResult = result.getOrElse {
            throw it
        }
        return mapperResult
    }
}
