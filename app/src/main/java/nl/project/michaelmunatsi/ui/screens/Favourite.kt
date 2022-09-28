package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import nl.project.michaelmunatsi.ui.layouts.NewsArticle
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.viewModel.NewsViewModel

object Favourite {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        newsViewModel: NewsViewModel = hiltViewModel(),
        onArticleTitleClick: (id: Int) -> Unit = {}
    ) {
//        val productPaging = newsViewModel.articles.collectAsLazyPagingItems()
//        // val state = viewModel.state
//        Surface(
//            modifier = modifier
//        ) {
//            Column(
//                modifier = modifier.padding(start = dimen.dp_16, end = dimen.dp_16)
//            ) {
//
//                // display list of  favorite article
//                LazyColumn(modifier = modifier) {
//                    items(productPaging) { item ->
//                        if (item != null) {
//                            NewsArticle.Layout(
//                                article = item,
//                                onArticleTitleClick = { onArticleTitleClick.invoke(item.Id) })
//                        }
//                    }
//                    // show progress bar
//                    item {
//                        ProgressBar.Layout()
//                    }
//                }
//            }
//        }
//    }
    }
}

