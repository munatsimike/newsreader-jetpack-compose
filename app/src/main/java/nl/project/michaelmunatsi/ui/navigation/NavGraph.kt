package nl.project.michaelmunatsi.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nl.project.michaelmunatsi.DetailPage
import nl.project.michaelmunatsi.ui.screens.MainScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsAppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            MainScreen(
                onMainClick = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = "details/{detailId}",
            arguments = listOf(navArgument("detailId") {
                type = NavType.IntType
                nullable = false
            })

        ) {
            DetailPage(
                onDetailClick = { route ->
                    navController.navigate(route)
                },
                detailId = it.arguments?.getInt("detailId") ?: -1,
            )
        }
    }
}