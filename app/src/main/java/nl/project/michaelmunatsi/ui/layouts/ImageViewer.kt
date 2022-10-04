package nl.project.michaelmunatsi.ui.layouts

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nl.project.michaelmunatsi.R

// contains code for displaying and image
object ImageViewer {
    @Composable
    fun Layout(modifier: Modifier = Modifier, imageUrl: String, size: Int, cornerRadius: Int = 0) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            modifier = modifier
                .size(size.dp)
                .clip(RoundedCornerShape(cornerRadius.dp)),
            placeholder = painterResource(id = R.drawable.ic_baseline_image_24),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
        )
    }
}