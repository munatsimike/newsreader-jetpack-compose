package nl.project.michaelmunatsi.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import nl.project.michaelmunatsi.ui.screens.Detail
import nl.project.michaelmunatsi.ui.screens.Favourite
import nl.project.michaelmunatsi.ui.screens.Main
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsAppNavGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    sharedUserViewModel: UserViewModel,
    sharedViewModel: NewsViewModel
) {

    NavHost(
        navController = navController,
        startDestination = NavigationDestination.Home.screen_route,
    ) {
        composable(
            route = NavigationDestination.Home.screen_route
        ) {
            Main.Screen(onTitleClick = { articleId ->
                navController.navigate(NavigationDestination.Detail.screen_route + "/$articleId")
            }, scaffoldState =scaffoldState, sharedNewsViewModel = sharedViewModel, sharedUserViewModel = sharedUserViewModel)
        }

        composable(route = NavigationDestination.Favourite.screen_route) {
            Favourite.Screen(onTitleClick = { articleId ->
                navController.navigate(NavigationDestination.Detail.screen_route + "/$articleId")
            }, scaffoldState =scaffoldState, sharedNewsViewModel = sharedViewModel, sharedUserViewModel = sharedUserViewModel)
        }

        composable(
            route = NavigationDestination.Detail.screen_route + "/{articleId}",
            arguments = listOf(navArgument("articleId") {
                type = NavType.IntType
            })

        ) {
            Detail.Screen(
                onBackBtnClick = {
                    navController.popBackStack()
                },
                articleId = it.arguments?.getInt("articleId") ?: -1,
                scaffoldState = scaffoldState,
                sharedUserViewModel = sharedUserViewModel,
                sharedNewsViewModel = sharedViewModel
            )
        }
    }
}


