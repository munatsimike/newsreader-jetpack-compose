package nl.project.michaelmunatsi.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import nl.project.michaelmunatsi.R

@Composable
fun ImageViewer(modifier: Modifier= Modifier, imageUrl: String, size: Int, cornerRadius: Int = 0){
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .size(size.dp)
            .clip(RoundedCornerShape(cornerRadius.dp)),
        placeholder = painterResource(id = R.drawable.ic_baseline_image_24),
        model = imageUrl,
        contentDescription = "Article image",
        contentScale = ContentScale.FillBounds,
    )
}