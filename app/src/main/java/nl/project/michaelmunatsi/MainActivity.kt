package nl.project.michaelmunatsi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.ui.DefaultSnackBar
import nl.project.michaelmunatsi.ui.navigation.BottomNavigationMenu
import nl.project.michaelmunatsi.ui.navigation.NavigationDestination
import nl.project.michaelmunatsi.ui.navigation.NewsAppNavGraph
import nl.project.michaelmunatsi.ui.navigation.NewsReaderToolBar
import nl.project.michaelmunatsi.ui.screens.LoginRegister
import nl.project.michaelmunatsi.ui.screens.Logout
import nl.project.michaelmunatsi.ui.theme.MichaelmunatsiTheme
import nl.project.michaelmunatsi.utils.MyUtility.InitResources
import nl.project.michaelmunatsi.utils.MyUtility.destinationFromUrl
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitResources()
            val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
            val topBarState = rememberSaveable { (mutableStateOf(true)) }

            MichaelmunatsiTheme(
                darkTheme = false
            ) {
                val animatedNavController = rememberAnimatedNavController()
                // is user navigating to the detail screen
                bottomBarState.value = onDestinationChange(
                    navController = animatedNavController, NavigationDestination.Detail
                )
                topBarState.value = bottomBarState.value

                val modalBottomSheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                val scaffoldState = rememberScaffoldState()

                Start(
                    navController = animatedNavController,
                    modalBottomSheetState = modalBottomSheetState,
                    topBarState = topBarState,
                    bottomBarState = bottomBarState,
                    scaffoldState = scaffoldState
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Start(
        modifier: Modifier = Modifier,
        navController: NavHostController,
        modalBottomSheetState: ModalBottomSheetState,
        topBarState: MutableState<Boolean>,
        bottomBarState: MutableState<Boolean>,
        scaffoldState: ScaffoldState,
        sharedUserViewModel: UserViewModel = hiltViewModel(),
        sharedNewsViewModel: NewsViewModel = hiltViewModel()
    ) {
        LaunchedEffect(Unit) {
            sharedUserViewModel.authToken.collectLatest {
                sharedUserViewModel.updateUserState(it)
            }
        }
        val userState by sharedUserViewModel.userState.collectAsState()
        ModalBottomSheetLayout(
            sheetContent = {
                when (userState) {
                    is UserState.LoggedIn -> {
                        Logout.Screen(sharedUserViewModel)
                    }
                    is UserState.LoggedOut -> {
                        LoginRegister.Screen(sharedUserViewModel)
                    }
                }
            },
            sheetState = modalBottomSheetState,
            sheetShape = RoundedCornerShape(topStart = dimen.dp_16, topEnd = dimen.dp_16),
            sheetBackgroundColor = Color.White,
        ) {
            Scaffold(topBar = {
                NewsReaderToolBar(
                    topBarState, modalBottomSheetState
                )
            }, bottomBar = {
                BottomNavigationMenu(
                    navController, bottomBarState, scaffoldState, sharedUserViewModel
                )
            }, scaffoldState = scaffoldState, snackbarHost = {
                scaffoldState.snackbarHostState
            }) { innerPadding ->
                Box(modifier = modifier.padding(innerPadding)) {
                    NewsAppNavGraph(
                        navController,
                        scaffoldState,
                        sharedUserViewModel,
                        sharedNewsViewModel
                    )
                    DefaultSnackBar(
                        snackbarHostState = scaffoldState.snackbarHostState, onAction = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.performAction()
                        }, modifier = modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
fun onDestinationChange(
    navController: NavHostController, destination: NavigationDestination
): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.let { destinationFromUrl(it) } != destination.screen_route
}