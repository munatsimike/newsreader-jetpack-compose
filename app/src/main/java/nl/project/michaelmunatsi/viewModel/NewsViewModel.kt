package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import nl.project.michaelmunatsi.data.NewsArticlePager
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val pager: NewsArticlePager) : ViewModel() {

    val article = Pager(PagingConfig(pageSize = 20)) {
        pager
    }.flow.cachedIn(viewModelScope)
}