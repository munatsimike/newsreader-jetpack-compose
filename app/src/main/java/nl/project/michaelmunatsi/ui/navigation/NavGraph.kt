package nl.project.michaelmunatsi.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import nl.project.michaelmunatsi.ui.screens.Detail
import nl.project.michaelmunatsi.ui.screens.Favourite
import nl.project.michaelmunatsi.ui.screens.Main
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
// contains code that builds the animated nav graph
fun NewsAppNavGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    sharedUserViewModel: UserViewModel,
    sharedViewModel: NewsViewModel
) {
    AnimatedNavHost(navController = navController,
        startDestination = NavigationDestination.Home.screen_route,
        enterTransition = {
            slideInHorizontally(animationSpec = tween(durationMillis = 800),
                initialOffsetX = { -15000 })
        },
        exitTransition = {
            slideOutHorizontally(animationSpec = tween(durationMillis = 0),
                targetOffsetX = { 1000 })
        },
        popEnterTransition = {
            slideInHorizontally(animationSpec = tween(durationMillis = 800),
                initialOffsetX = { -15000 })
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = tween(durationMillis = 0),
                targetOffsetX = { 1000 })
        }) {
        composable(
            route = NavigationDestination.Home.screen_route,
        ) {
            Main.Screen(
                onTitleClick = { articleId ->
                    navController.navigate(NavigationDestination.Detail.screen_route + "/$articleId")
                },
                scaffoldState = scaffoldState,
                sharedNewsViewModel = sharedViewModel,
                sharedUserViewModel = sharedUserViewModel
            )
        }

        composable(route = NavigationDestination.Favourite.screen_route) {
            Favourite.Screen(
                onTitleClick = { articleId ->
                    navController.navigate(NavigationDestination.Detail.screen_route + "/$articleId")
                },
                scaffoldState = scaffoldState,
                sharedNewsViewModel = sharedViewModel,
                sharedUserViewModel = sharedUserViewModel,
                navigateToHomeScreen = {
                    navController.navigate(NavigationDestination.Home.screen_route)
                }
            )
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


