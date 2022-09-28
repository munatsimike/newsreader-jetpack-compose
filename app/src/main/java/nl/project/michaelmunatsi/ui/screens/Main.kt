package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.ui.layouts.NewsArticle.Layout
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

object Main {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        newsViewModel: NewsViewModel = hiltViewModel(),
        userViewModel: UserViewModel = hiltViewModel(),
        scaffoldState: ScaffoldState,
        onTitleClick: (id: Int) -> Unit = {}
    ) {
        val scope = rememberCoroutineScope()
        Surface {
            Column(modifier = Modifier.padding(start = dimen.dp_16, end = dimen.dp_16)) {
                val articles = newsViewModel.pagingData.collectAsLazyPagingItems()
                // display list of article
                LazyColumn {
                    items(articles) { item ->
                        if (item != null) {
                            Layout(
                                article = item,
                                onArticleTitleClick = { onTitleClick.invoke(item.Id) },
                                scaffoldState = scaffoldState,
                                userViewModel = userViewModel
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
                            ) { articles.refresh() }
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
                            ) { articles.refresh() }
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