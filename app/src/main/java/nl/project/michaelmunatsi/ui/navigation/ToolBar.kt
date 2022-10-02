package nl.project.michaelmunatsi.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsReaderToolBar(
    topBarState: MutableState<Boolean>,
    modalBottomSheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
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
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "")
                }
            })
        })
}