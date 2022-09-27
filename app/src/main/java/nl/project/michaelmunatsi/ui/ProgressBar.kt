package nl.project.michaelmunatsi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.project.michaelmunatsi.utils.MyUtility.dimen

// show progress bar
@Composable
fun ShowProgressBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimen.dp_10),
        horizontalArrangement = Arrangement.Center
    )
    {
        CircularProgressIndicator()
    }
}
