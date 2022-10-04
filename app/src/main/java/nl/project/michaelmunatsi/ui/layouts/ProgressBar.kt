package nl.project.michaelmunatsi.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// contains code for showing progress bar
object ProgressBar {
    @Composable
    fun Show(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(15.dp),
            horizontalArrangement = Arrangement.Center
        )
        {
            CircularProgressIndicator(
                color = MaterialTheme.colors.secondary
            )
        }
    }
}
