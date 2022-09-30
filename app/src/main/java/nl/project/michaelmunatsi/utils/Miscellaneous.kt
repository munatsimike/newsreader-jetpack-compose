package nl.project.michaelmunatsi.utils

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
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
    fun UrlLinkBuilder(
        modifier: Modifier = Modifier,
        url: String,
        index: String = "",
        text: String = url
    ) {
        val uriHandler = LocalUriHandler.current
        Row(modifier=modifier.padding(6.dp)) {
            Text(text = index)
            Spacer(modifier = modifier.width(6.dp))
            Text(
                modifier = modifier
                    .clickable {
                        uriHandler.openUri(url)
                    },

                text = text,
                color = Color.Blue
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
}
