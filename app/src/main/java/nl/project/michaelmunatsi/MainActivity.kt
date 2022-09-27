package nl.project.michaelmunatsi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.ui.navigation.BottomNavigationMenuBuilder
import nl.project.michaelmunatsi.ui.navigation.NavigationDestination
import nl.project.michaelmunatsi.ui.navigation.NewsAppNavGraph
import nl.project.michaelmunatsi.ui.navigation.NewsReaderToolBar
import nl.project.michaelmunatsi.ui.screens.LoginRegister
import nl.project.michaelmunatsi.ui.screens.Logout
import nl.project.michaelmunatsi.ui.theme.MichaelmunatsiTheme
import nl.project.michaelmunatsi.utils.MyUtility.InitResources
import nl.project.michaelmunatsi.utils.MyUtility.destinationFromUrl
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.viewModel.UserViewModel

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitResources()
            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
            val topBarState = rememberSaveable { (mutableStateOf(true)) }

            MichaelmunatsiTheme(
                darkTheme = false
            ) {
                val navController = rememberNavController()
                bottomBarState.value = topBottomBarVisibility(navController = navController)
                topBarState.value = bottomBarState.value

                val modalBottomSheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

                Start(
                    navController = navController,
                    modalBottomSheetState = modalBottomSheetState,
                    topBarState = topBarState,
                    bottomBarState = bottomBarState
                )
            }
        }
    }

    @Composable
    fun topBottomBarVisibility(navController: NavHostController): Boolean {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route?.let { destinationFromUrl(it) } != NavigationDestination.Detail.screen_route
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Start(
        navController: NavHostController,
        modalBottomSheetState: ModalBottomSheetState,
        topBarState: MutableState<Boolean>,
        bottomBarState: MutableState<Boolean>,
        userViewModel: UserViewModel = hiltViewModel()
    ) {
        var token by rememberSaveable { mutableStateOf<Token?>(null) }

        LaunchedEffect(Unit) {
            userViewModel.authToken.collectLatest {
                token = it
            }
        }

        ModalBottomSheetLayout(
            sheetContent = {
                if (token == null) {
                    LoginRegister.Screen()
                } else {
                    Logout.Screen()
                }
            },

            sheetState = modalBottomSheetState,
            sheetShape = RoundedCornerShape(topStart = dimen.dp_16, topEnd = dimen.dp_16),
            sheetBackgroundColor = Color.White,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
            ) {
                Scaffold(topBar = {
                    NewsReaderToolBar(
                        topBarState, modalBottomSheetState
                    )
                }, bottomBar = {
                    BottomNavigationMenuBuilder(
                        navController, bottomBarState
                    )
                }) { padding ->
                    NewsAppNavGraph(navController, padding)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MichaelmunatsiTheme {

    }
}