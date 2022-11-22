package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
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
                LazyColumn {
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
                            ) { sharedNewsViewModel.loadFavorites() }
                        }
                        NetworkState.Loading -> {
                            item {
                                ProgressBar.Show()
                            }
                        }
                        NetworkState.NotLoading -> {}
                    }
                }

                if ((networkState as NetworkState.Success).data.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.favourite_folder_empty),
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}