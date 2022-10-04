package nl.project.michaelmunatsi.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.utils.MyUtility.resource
import java.util.*

// contans code for all destination names, routes and icons
sealed class NavigationDestination(
    val destination: String,
    val screen_route: String,
    val icon: ImageVector? = null,
) {
    object Home : NavigationDestination(
        destination = resource.getString(R.string.navigation_destination_home),
        screen_route = resource.getString(R.string.navigation_destination_home)
            .lowercase(Locale.ROOT),
        icon = Icons.Default.Home
    )

    object Favourite : NavigationDestination(
        destination = resource.getString(R.string.navigation_destination_favourite),
        screen_route = resource.getString(R.string.navigation_destination_favourite).lowercase(
            Locale.ROOT
        ),
        Icons.Default.Favorite
    )

    object Detail : NavigationDestination(
        destination = resource.getString(R.string.navigation_destination_detail),
        screen_route = resource.getString(R.string.navigation_destination_detail)
            .lowercase(Locale.ROOT)
    )
}