package nl.project.michaelmunatsi.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingSource
import androidx.paging.PagingState
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import javax.inject.Inject

class NewsArticlePager @Inject constructor(
    private val newsRepo: NewsRepository,
    private val newsMapper: NewsArticleMapper
) : PagingSource<Int, NewsArticle>() {

    var state by mutableStateOf(PagingState())
    private var nextId: Int = 0

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val result = fetch(params.key).getOrElse {
            return LoadResult.Error(it)
        }
        // key
        return LoadResult.Page(result, null, nextKey = nextId)
    }

    private suspend fun fetch(key: Int?): Result<List<NewsArticle>> {
        val response: Result<List<NewsArticle>>
        val result: MyAPiResponse
        if (key == 0) {
            result = newsRepo.fetchFirstArticleBatch()
            response = newsMapper.mapList(result.Results)
        } else {
            result = newsRepo.getMoreArticles(nextId)
            response = newsMapper.mapList(result.Results)
        }
        nextId = result.NextId
        return response
    }
}

data class PagingState(
    var isLoading: Boolean = false,
    var notLoading: Boolean = false,
    val error: String? = null,
)
