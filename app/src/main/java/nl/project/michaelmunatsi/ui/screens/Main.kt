package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
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
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.ui.layouts.Article
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.ui.showSnackBar
import nl.project.michaelmunatsi.utils.MyUtility
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel
import retrofit2.HttpException
import java.io.IOException

object Main {

    @OptIn(ExperimentalMaterialApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        onTitleClick: (id: Int) -> Unit = {}
    ) {
        val userState by sharedUserViewModel.userState.collectAsState()
        val articles = sharedNewsViewModel.allArticles.collectAsLazyPagingItems()
        val refresh by sharedNewsViewModel.refresh.observeAsState()
        val scope = rememberCoroutineScope()
        val swipeRefreshState = rememberSwipeRefreshState(false)
        var snackBarMessage by remember { mutableStateOf("") }

        if (refresh == true) {
            articles.refresh()
            sharedNewsViewModel.refresh(false)
        }

        LaunchedEffect(Unit) {
            snapshotFlow { userState }.collect {
                articles.refresh()
            }
        }

        // collect network state
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
                                onArticleTitleClick = { onTitleClick.invoke(item.Id) },
                                scaffoldState = scaffoldState,
                                userState = userState,
                                sharedViewModel = sharedNewsViewModel,
                            )
                        }
                    }

                    when (val loadState = articles.loadState.append) {
                        is LoadState.Error -> {
                            if (snackBarMessage == "") {
                                snackBarMessage = errorMsg(loadState.error)
                                showSnackBar(
                                    message = snackBarMessage,
                                    coroutineScope = scope,
                                    scaffoldState = scaffoldState,
                                    actionLabel = MyUtility.resource.getString(R.string.retry)
                                ) {
                                    articles.retry()
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
                                    snackBarMessage = ""
                                    ProgressBar.Show()
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
            //detect errors from paging source
            when (val loadState = articles.loadState.refresh) {
                is LoadState.Error -> {
                    if (snackBarMessage == "") {
                        snackBarMessage = errorMsg(loadState.error)
                        showSnackBar(
                            message = snackBarMessage,
                            coroutineScope = scope,
                            scaffoldState = scaffoldState,
                            actionLabel = MyUtility.resource.getString(R.string.retry)
                        ) {
                            articles.retry()
                        }
                    }
                }
                is LoadState.Loading -> {
                    Row {
                        snackBarMessage = ""
                        ProgressBar.Show()
                    }
                }
                else -> {

                }
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
