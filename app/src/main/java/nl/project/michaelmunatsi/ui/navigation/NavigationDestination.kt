package nl.project.michaelmunatsi.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationDestination(
    val title: String,
    val screen_route: String,
    val icon: ImageVector? = null,

    ) {
    object Home : NavigationDestination("Home", "home", Icons.Default.Home)
    object Favourite : NavigationDestination("Favourite", "favourite", Icons.Default.Favorite)
    object Detail : NavigationDestination("Detail", "detail")
}