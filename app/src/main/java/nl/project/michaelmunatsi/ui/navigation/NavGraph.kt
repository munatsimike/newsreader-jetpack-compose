package nl.project.michaelmunatsi.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.navigation.animation.AnimatedNavHost
import nl.project.michaelmunatsi.model.NewsArticle
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
                sharedUserViewModel = sharedUserViewModel
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


