package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import nl.project.michaelmunatsi.ui.layouts.NewsArticle
import nl.project.michaelmunatsi.ui.layouts.ProgressBar
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.viewModel.NewsViewModel

object Main {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        newsViewModel: NewsViewModel = hiltViewModel(),
        onTitleClick: (id: Int)->Unit = {}
    ) {
        // val product by viewModel.myList.collectAsState()
        val productPaging = newsViewModel.articles.collectAsLazyPagingItems()
        // val state = viewModel.state
        Surface(
            modifier = modifier.background(Color.Green)
        ) {
            Column(
                modifier = modifier.padding(dimen.dp_25)
            ) {

                // display list of article
                LazyColumn(modifier = modifier) {
                    items(productPaging) { item ->
                        if (item != null) {
                            NewsArticle.Layout(article = item, onArticleTitleClick = { onTitleClick.invoke(item.Id) })
                        }
                    }
                    // show progress bar
                    item {
                        ProgressBar.Layout()
                    }
                }
            }
        }
    }
}


