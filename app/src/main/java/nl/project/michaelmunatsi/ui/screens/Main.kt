package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collectLatest
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.ui.layouts.Article
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel
import retrofit2.HttpException
import java.io.IOException

// code to display articles on the main screen. The main screen fetches articles from room database
object Main {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        onTitleClick: (id: Int) -> Unit = {}
    ) {
        // Collect user state which can be logged in or logged out.Depending on the user state a user can like or dislike any article displayed in the list
        val userState by sharedUserViewModel.userState.collectAsState()
        // collect articles from room
        val articles = sharedNewsViewModel.allArticles.collectAsLazyPagingItems()
        val refresh by sharedNewsViewModel.refresh.observeAsState()
        val scope = rememberCoroutineScope()
        val swipeRefreshState = rememberSwipeRefreshState(false)

        // check if refresh is required. if a user logs out from the favourite, he will be directed to the this home screen. A refresh will be required to clear all liked articles
        if (refresh == true) {
            articles.refresh()
            sharedNewsViewModel.refresh(false)
        }

        // refresh articles if userstate changes from logged in to logged out or vice versa
        LaunchedEffect(Unit) {
            snapshotFlow { userState }.collect {
                articles.refresh()
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    articles.refresh()
                },
            ) {
                // display list of article
                LazyColumn {
                    items(articles) { item ->
                        item?.let {
                            // display news article card
                            Article.Layout(
                                article = item,
                                onArticleTitleClick = {
                                    if (articles.loadState.refresh != LoadState.Loading) {
                                        onTitleClick.invoke(item.Id)
                                    }
                                },
                                scaffoldState = scaffoldState,
                                userState = userState,
                                sharedViewModel = sharedNewsViewModel,
                            )
                        }
                    }

                    when (val loadState = articles.loadState.append) {
                        is LoadState.Error -> {
                            this.item {
                                LaunchedEffect(Unit) {
                                    snapshotFlow { errorMsg(loadState.error) }.collectLatest {
                                        showSnackBar(
                                            message = errorMsg(loadState.error),
                                            coroutineScope = scope,
                                            scaffoldState = scaffoldState,
                                            actionLabel = MyUtility.resource.getString(R.string.retry)
                                        ) {
                                            articles.retry()
                                        }
                                    }
                                }
                            }
                        }
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .background(Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ProgressBar.Show()
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            when (val loadState = articles.loadState.refresh) {
                is LoadState.Error -> {
                    LaunchedEffect(Unit) {
                        snapshotFlow { errorMsg(loadState.error) }.collectLatest {
                            showSnackBar(
                                message = errorMsg(loadState.error),
                                coroutineScope = scope,
                                scaffoldState = scaffoldState,
                                actionLabel = MyUtility.resource.getString(R.string.retry)
                            ) {
                                articles.retry()
                            }
                        }
                    }
                }
                is LoadState.Loading -> {
                    Row {
                        ProgressBar.Show()
                    }
                }
                else -> {}
            }
        }
    }
}

private fun errorMsg(throwable: Throwable): String {
    return when (throwable) {
        is IOException -> {
            MyUtility.resource.getString(R.string.No_internet_access)
        }
        is HttpException -> {
            MyUtility.resource.getString(R.string.user_not_logged_in)
        }
        else -> {
            MyUtility.resource.getString(R.string.No_internet_access)
        }
    }
}


