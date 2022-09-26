package nl.project.michaelmunatsi.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationMenuBuilder(
    navController: NavController,
    bottomBarState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    items: List<NavigationDestination> = listOf(
        NavigationDestination.Home, NavigationDestination.Favourite
    ),
    selectedItemColor: Color = Color.Red,
    unselectedIItemColor: Color = Color.White
) {
    AnimatedVisibility(visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomNavigation(modifier = modifier) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    BottomNavigationItem(icon = {
                        item.icon?.let {
                            Icon(
                                imageVector = it, contentDescription = item.destination
                            )
                        }
                    },
                        label = {
                            Text(text = item.destination)
                        },
                        selectedContentColor = selectedItemColor,
                        unselectedContentColor = unselectedIItemColor,
                        selected = currentRoute == item.screen_route,
                        onClick = {
                            navController.navigate(item.screen_route) {

                                navController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(screen_route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }
            }
        })
}