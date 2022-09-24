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
import nl.project.michaelmunatsi.ui.NewsArticleLayout
import nl.project.michaelmunatsi.ui.ShowProgressBar
import nl.project.michaelmunatsi.viewModel.NewsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onMainClick: (route: String) -> Unit,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    // val product by viewModel.myList.collectAsState()

    val productPaging = newsViewModel.article.collectAsLazyPagingItems()
    // val state = viewModel.state
    Column(
        modifier = modifier.padding(25.dp)
    ) {

        // display list of article
        LazyColumn(modifier = modifier) {
            items(productPaging) { item ->
                if (item != null) {
                    NewsArticleLayout(article = item, onMainClick = onMainClick)
                }
            }
            // show progress bar
            item {
                ShowProgressBar()
            }
        }
    }
}


