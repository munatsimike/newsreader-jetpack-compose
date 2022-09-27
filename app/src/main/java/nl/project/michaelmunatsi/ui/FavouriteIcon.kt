package nl.project.michaelmunatsi.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.R

@Composable
fun LikeDislikeIcon(modifier: Modifier = Modifier, initState: Boolean = false, size:Int = 35) {
    val isChecked = remember { mutableStateOf(initState) }
    IconToggleButton(

        checked = isChecked.value,
        onCheckedChange = {
            isChecked.value = !isChecked.value
        },
    ) {
        Icon(
            imageVector = if (isChecked.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(id = R.string.favourite_icon),
            tint = if (isChecked.value) Color(0xFFE64A19) else Color(0xFFE64A19),

            modifier = modifier.size(size.dp)
        )
    }
}
