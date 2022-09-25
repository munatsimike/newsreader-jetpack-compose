package nl.project.michaelmunatsi.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import nl.project.michaelmunatsi.ui.screens.Detail
import nl.project.michaelmunatsi.ui.screens.Favourite
import nl.project.michaelmunatsi.ui.screens.Main

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsAppNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {

    NavHost(
        navController = navController,
        startDestination = NavigationDestination.Home.screen_route,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(route = NavigationDestination.Home.screen_route) {
            Main.Screen(onTitleClick = {
                navController.navigate(NavigationDestination.Detail.screen_route)
            })
        }

        composable(route = NavigationDestination.Favourite.screen_route) {
            Favourite.Screen(onArticleTitleClick = {
                navController.navigate(NavigationDestination.Detail.screen_route)
            })
        }

        composable(
            route = NavigationDestination.Detail.screen_route, arguments = listOf(navArgument("detailId") {
                type = NavType.StringType
                nullable = true
            })

        ) {
            Detail.Screen(
                onBackBtnClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

//private fun NavHostController.navigateToSingleAccount(accountType: String) {
//    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
//}
