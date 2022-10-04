package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.ui.layouts.Article
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

abstract class BaseScreen {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun DisplayArticles(
        articles: LazyPagingItems<NewsArticle>,
        sharedNewsViewModel: NewsViewModel,
        sharedUserViewModel: UserViewModel,
        scaffoldState: ScaffoldState,
        onTitleClick: (id: Int) -> Unit,
    ) {
        val scope = rememberCoroutineScope()
        val swipeRefreshState = rememberSwipeRefreshState(false)
        val scrollState = rememberLazyListState()
        saveListPosition(scrollState, sharedNewsViewModel)
        Surface(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { articles.refresh() },
            ) {
                // display list of article
                LazyColumn(
                    state = scrollState
                ) {
                    returnToPreviousListPosition(scrollState, scope, sharedNewsViewModel)
                    items(articles) { item ->
                        if (item != null) {
                            Article.Layout(
                                article = item,
                                onArticleTitleClick = { onTitleClick.invoke(item.Id) },
                                scaffoldState = scaffoldState,
                                userViewModel = sharedUserViewModel,
                                sharedViewModel = sharedNewsViewModel,
                            )
                        }
                    }

//                    when (articles.loadState.append) {
//                        is LoadState.Error -> {
//                            showSnackBar(
//                                message = (articles.loadState.append as LoadState.Error).error.message.toString(),
//                                coroutineScope = scope,
//                                scaffoldState = scaffoldState,
//                                actionLabel = MyUtility.resource.getString(R.string.retry)
//                            ) { articles.retry() }
//                        }
//                        LoadState.Loading -> {
//                            item {
//                                ProgressBar.Show()
//                            }
//                        }
//                        is LoadState.NotLoading -> {}
//                    }

                    when (articles.loadState.refresh) {
                        is LoadState.Error -> {
                            val errorMessage =
                                (articles.loadState.refresh as LoadState.Error).error.message.toString()
                            showSnackBar(
                                message = errorMessage,
                                coroutineScope = scope,
                                scaffoldState = scaffoldState,
                                actionLabel = MyUtility.resource.getString(R.string.retry)
                            ) { articles.retry() }
                        }
                        LoadState.Loading -> {
                            item {
                                ProgressBar.Show()
                            }
                        }
                        is LoadState.NotLoading -> {}
                    }
                }
            }
        }
    }
}

private fun saveListPosition(scrollState: LazyListState, sharedNewsViewModel: NewsViewModel) {
    if (scrollState.isScrollInProgress) {
        sharedNewsViewModel.lazyColumnScrollPosition =
            scrollState.firstVisibleItemIndex
    }
}

private fun returnToPreviousListPosition(
    scrollState: LazyListState,
    scope: CoroutineScope,
    sharedNewsViewModel: NewsViewModel
) {
    scope.launch {
        scrollState.scrollToItem(sharedNewsViewModel.lazyColumnScrollPosition)
    }
}