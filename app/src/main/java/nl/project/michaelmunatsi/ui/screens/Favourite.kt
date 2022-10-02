package nl.project.michaelmunatsi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import nl.project.michaelmunatsi.viewModel.NewsViewModel
import nl.project.michaelmunatsi.viewModel.UserViewModel

object Favourite {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        sharedNewsViewModel: NewsViewModel,
        scaffoldState: ScaffoldState,
        onTitleClick: (id: Int) -> Unit = {}
    ) {
        val articles = sharedNewsViewModel.likedArticle.collectAsLazyPagingItems()
        Main.DisplayArticles(
                articles, sharedNewsViewModel, sharedUserViewModel, scaffoldState, onTitleClick
            )
    }
}