package nl.project.michaelmunatsi.ui.layouts

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
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.ui.theme.Orange

object LikeDisLike {

    @Composable
    fun Layout(modifier: Modifier = Modifier, initState: Boolean = false) {
        val isChecked = remember { mutableStateOf(initState) }
        IconToggleButton(

            checked = isChecked.value,
            onCheckedChange = {
                isChecked.value = !isChecked.value
            },
        ) {
            Icon(
                imageVector = if (isChecked.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Icon",
                tint = if (isChecked.value) Orange else Orange,

                modifier = modifier.size(35.dp)
            )
        }
    }
}
