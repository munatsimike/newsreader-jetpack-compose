package nl.project.michaelmunatsi.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingSource
import androidx.paging.PagingState
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import javax.inject.Inject

class NewsArticlePager @Inject constructor(
    private val newsRepo: NewsRepository,
    private val newsMapper: NewsArticleMapper
) : PagingSource<Int, NewsArticle>() {

    var state by mutableStateOf(PagingState())

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val result = fetch().getOrElse {
            return LoadResult.Error(it)
        }
        // key
        return LoadResult.Page(result, null, (params.key ?: 0) + 1)
    }

    private suspend fun fetch(): Result<List<NewsArticle>> {
        val response = newsRepo.fetchFirstArticleBatch()
         return newsMapper.mapList(response.Results)
    }
}

data class PagingState(
    var isLoading: Boolean = false,
    var notLoading: Boolean = false,
    val error: String? = null,
)
