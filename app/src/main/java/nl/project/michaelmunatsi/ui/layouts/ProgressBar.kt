package nl.project.michaelmunatsi.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// show progress bar

object ProgressBar {
    @Composable
    fun Layout(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        )
        {
            CircularProgressIndicator()
        }
    }
}
