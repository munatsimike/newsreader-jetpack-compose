package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.state.NetworkState
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.ui.layouts.Article
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

// code for displaying liked articles. Liked articles will be fetched from the remote server
object Favourite {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        modifier: Modifier = Modifier,
        navigateToHomeScreen: () -> Unit,
        onTitleClick: (id: Int) -> Unit,
    ) {
        val scope = rememberCoroutineScope()
        // userstate can be logged in or logged out.
        // Depending on the userstate a user can like or dislike any article displayed in the list
        val userState by sharedUserViewModel.userState.collectAsState()
        val swipeRefreshState = rememberSwipeRefreshState(false)
        // collect network state, network state can be loading, error or success
        val networkState by sharedNewsViewModel.networkState.collectAsState()
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    sharedNewsViewModel.loadFavorites()
                },
            ) {
                // display list of article
                LazyColumn {
                    when (networkState) {
                        is NetworkState.Success -> {
                            val data = (networkState as NetworkState.Success).data
                            items(data) { item ->
                                // display news article card
                                Article.Layout(
                                    article = item,
                                    onArticleTitleClick = { onTitleClick.invoke(item.Id) },
                                    scaffoldState = scaffoldState,
                                    userState = userState,
                                    sharedViewModel = sharedNewsViewModel,
                                )
                            }

                            item {
                                //check if there are not liked article and notify user
                                if (data.isEmpty()) {
                                    Column(modifier = modifier.fillParentMaxSize()) {
                                        ShowNoFavoritesMessage()
                                    }
                                }
                            }
                        }
                        // show error message
                        is NetworkState.Error -> {
                            showSnackBar(
                                message = (networkState as NetworkState.Error).error.toString(),
                                coroutineScope = scope,
                                scaffoldState = scaffoldState,
                                actionLabel = resource.getString(R.string.retry)
                            ) { sharedNewsViewModel.loadFavorites() }
                        }
                        // show progress bar
                        NetworkState.Loading -> {
                            item {
                                ProgressBar.Show()
                            }
                        }
                        NetworkState.NotLoading -> {}
                    }
                }
            }
        }
        // redirect user to the home screen if he logs out from the favourite screen
        when (userState) {
            is UserState.LoggedOut -> {
                // marker  for the home screen to know that user logged out from the favourite screen so a refresh is required to clear all liked articles
                sharedNewsViewModel.refresh(true)
                navigateToHomeScreen.invoke()
            }
            else -> {}
        }
    }

    // show message to tell user that there no like articles
    @Composable
    private fun ShowNoFavoritesMessage(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = modifier
                    .height(100.dp)
                    .width(100.dp),
                painter = painterResource(id = R.drawable.heart),
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.favourite_folder_empty), fontSize = 22.sp
            )
            Text(
                text = stringResource(id = R.string.like_articles), fontSize = 15.sp
            )
        }
    }
}