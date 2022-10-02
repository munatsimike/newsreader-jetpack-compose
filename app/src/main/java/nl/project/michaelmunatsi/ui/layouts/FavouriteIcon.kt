package nl.project.michaelmunatsi.ui.layouts

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.ui.theme.Orange
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.UserViewModel

object LikeDisLikeArticle {

    @Composable
    fun Layout(
        modifier: Modifier = Modifier,
        isChecked: Boolean,
        scaffoldState: ScaffoldState,
        userViewModel: UserViewModel,
        onLikeDislike: ()->Unit,

    ) {
        var checked by remember { mutableStateOf(isChecked) }
        val userState by userViewModel.userState.collectAsState()
        val scope = rememberCoroutineScope()
        IconToggleButton(
            checked = checked,
            onCheckedChange = {
                if (userState == UserState.LoggedIn) {
                   onLikeDislike.invoke()
                    checked = !checked
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
                imageVector = if (checked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Icon",
                tint = if (checked) Orange else Orange,
                modifier = modifier.size(35.dp)
            )
        }
    }
}
