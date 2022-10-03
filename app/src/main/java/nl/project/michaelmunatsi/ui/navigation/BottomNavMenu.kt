package nl.project.michaelmunatsi.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.UserViewModel

@Composable
fun BottomNavigationMenu(
    navController: NavController,
    bottomBarState: MutableState<Boolean>,
    scaffoldState: ScaffoldState,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,

    ) {
    val items: List<NavigationDestination> = listOf(
        NavigationDestination.Home, NavigationDestination.Favourite
    )
    val selectedItemColor: Color = MaterialTheme.colors.secondary
    val unselectedIItemColor: Color = Color.White

    val userState by userViewModel.userState.collectAsState()
    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = bottomBarState.value,
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
                            if (NavigationDestination.Favourite.destination == item.destination && userState == UserState.LoggedOut) {
                                showSnackBar(
                                    message = resource.getString(R.string.user_not_logged_in),
                                    coroutineScope = scope,
                                    scaffoldState = scaffoldState
                                )
                            } else {
                                navController.navigate(item.screen_route) {

                                    navController.graph.startDestinationRoute?.let { screen_route ->
                                        popUpTo(screen_route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        })
                }
            }
        })
}