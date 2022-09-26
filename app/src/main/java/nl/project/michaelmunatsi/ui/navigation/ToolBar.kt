package nl.project.michaelmunatsi.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.R

@Composable
fun NewsReaderToolBar(
    topBarState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            TopAppBar(title = {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.app_title),
                    textAlign = TextAlign.Center,
                )
            }, actions = {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "")

                    DropdownMenu(
                        expanded = false,
                        onDismissRequest = { false },
                        offset = DpOffset(x = 0.dp, y = 4.dp)
                    ) {
                        DropdownMenuItem(
                            onClick = { false }
                        ) {
                            Text(text = "Theme")
                        }
                    }
                }
            })
        })
}