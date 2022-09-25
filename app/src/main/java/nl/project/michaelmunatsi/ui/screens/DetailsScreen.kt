package nl.project.michaelmunatsi.ui.screens

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

object Detail {
    @Composable
    fun Screen(onBackBtnClick: ()->Unit) {
        TextButton(onClick = onBackBtnClick) {
        Text(text = "I am  the details page")
        }
    }
}