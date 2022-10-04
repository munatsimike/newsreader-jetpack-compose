package nl.project.michaelmunatsi.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.utils.MyUtility.dimen

// this contains code for the snack bar
@Composable
fun DefaultSnackBar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(dimen.dp_16),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(
                            onClick = onAction
                        ) {
                            Text(
                                text = actionLabel,
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

// this function displays the snackbar
fun showSnackBar(
    message: String,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    actionLabel: String? = null,
    action: () -> Unit = {},
) {
    if (message != "null") {
        coroutineScope.launch {
            val snackbarResult = message.let {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = it,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Long,
                )
            }
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {

                }
                SnackbarResult.ActionPerformed -> {

                    action.invoke()
                }

                else -> {}
            }
        }
    }
}