package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.state.NetworkState
import nl.project.michaelmunatsi.ui.layouts.Article
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

// contains code for displaying liked articles
object Favourite {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        onTitleClick: (id: Int) -> Unit = {}
    ) {
        val scope = rememberCoroutineScope()
        val userState by sharedUserViewModel.userState.collectAsState()
        val swipeRefreshState = rememberSwipeRefreshState(false)
        var snackBarMessage by remember { mutableStateOf("") }
        // collect network state
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
                LazyColumn(
                ) {
                    when (networkState) {
                        is NetworkState.Success -> {
                            items((networkState as NetworkState.Success).data) { item ->
                                // display news article card
                                Article.Layout(
                                    article = item,
                                    onArticleTitleClick = { onTitleClick.invoke(item.Id) },
                                    scaffoldState = scaffoldState,
                                    userState = userState,
                                    sharedViewModel = sharedNewsViewModel,
                                )
                            }
                        }
                        is NetworkState.Error -> {
                            showSnackBar(
                                message = (networkState as NetworkState.Error).error.toString(),
                                coroutineScope = scope,
                                scaffoldState = scaffoldState,
                                actionLabel = resource.getString(R.string.retry)
                            ) { sharedNewsViewModel.loadFavorites()}
                        }
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
    }
}