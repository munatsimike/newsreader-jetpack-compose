package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.ui.layouts.NewsArticle.Layout
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

object Main {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        onTitleClick: (id: Int) -> Unit = {}
    ) {
        val scope = rememberCoroutineScope()
        val articles = sharedNewsViewModel.pagingData.collectAsLazyPagingItems()
        val swipeRefreshState = rememberSwipeRefreshState(false)

        Row(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            Column(
            )
            {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { articles.refresh() },
                ) {
                    // display list of article
                    LazyColumn {
                        items(articles) { item ->
                            if (item != null) {
                                Layout(
                                    article = item,
                                    onArticleTitleClick = { onTitleClick.invoke(item.Id) },
                                    scaffoldState = scaffoldState,
                                    userViewModel = sharedUserViewModel,
                                    sharedViewModel = sharedNewsViewModel
                                )
                            }
                        }

                        when (articles.loadState.append) {
                            is LoadState.Error -> {
                                showSnackBar(
                                    message = (articles.loadState.append as LoadState.Error).error.message.toString(),
                                    coroutineScope = scope,
                                    scaffoldState = scaffoldState,
                                    actionLabel = resource.getString(R.string.retry)
                                ) { articles.retry()   }
                            }
                            LoadState.Loading -> {
                                item { ProgressBar.Show() }
                            }
                            is LoadState.NotLoading -> {}
                        }

                        when (articles.loadState.refresh) {
                            is LoadState.Error -> {
                                showSnackBar(
                                    message = (articles.loadState.refresh as LoadState.Error).error.message.toString(),
                                    coroutineScope = scope,
                                    scaffoldState = scaffoldState,
                                    actionLabel = resource.getString(R.string.retry)
                                ) { articles.retry() }
                            }
                            LoadState.Loading -> {
                                item { ProgressBar.Show() }
                            }
                            is LoadState.NotLoading -> {}
                        }
                    }
                }
            }
        }
    }
}