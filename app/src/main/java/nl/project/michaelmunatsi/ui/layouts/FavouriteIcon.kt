package nl.project.michaelmunatsi.ui.layouts

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility.resource

// contains code for the favourite icon
object LikeDisLikeArticle {

    @Composable
    fun Layout(
        modifier: Modifier = Modifier,
        isChecked: Boolean,
        scaffoldState: ScaffoldState,
        userState: UserState,
        onLikeDislike: () -> Unit,
    ) {
        val scope = rememberCoroutineScope()
        IconToggleButton(
            checked = isChecked,
            onCheckedChange = {
                if (userState == UserState.LoggedIn) {
                    onLikeDislike.invoke()
                } else {
                    showSnackBar(
                        message = resource.getString(R.string.user_not_logged_in),
                        coroutineScope = scope,
                        scaffoldState = scaffoldState
                    )
                }
            },
        ) {
            Icon(
                imageVector = if (isChecked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Icon",
                tint = if (isChecked) MaterialTheme.colors.secondary else MaterialTheme.colors.secondary,
                modifier = modifier.size(35.dp)
            )
        }
    }
}
