package nl.project.michaelmunatsi.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import nl.project.michaelmunatsi.data.remote.NewsApi
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import javax.inject.Inject

class LikedArticlePager @Inject constructor(
    private val newsMapper: NewsArticleMapper,
) : PagingSource<Int, NewsArticle>() {

    private var nextId: Int? = null

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val currentPage = params.key ?: 1
        return try {
            val data = fetch()
            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (nextId == 0) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun fetch(): List<NewsArticle> {
        val result = NewsApi.retrofitService.likedArticles()
        nextId = result.NextId
        val response = newsMapper.mapList(result.Results)
        return getMapperResult(response)
    }

    private fun getMapperResult(result: Result<List<NewsArticle>>): List<NewsArticle> {
        val mapperResult = result.getOrElse {
            throw it
        }
        return mapperResult
    }
}
