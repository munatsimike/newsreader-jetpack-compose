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

fun showSnackBar(
    message: String,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    actionLabel: String? = null,
    action: () -> Unit = {},
) {
    coroutineScope.launch {
        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Long,
        )
        when (snackbarResult) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> {
                action.invoke()
            }
        }
    }

}