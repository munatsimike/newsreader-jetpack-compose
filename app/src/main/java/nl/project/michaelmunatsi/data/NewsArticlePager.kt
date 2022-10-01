package nl.project.michaelmunatsi.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import javax.inject.Inject

class NewsArticlePager @Inject constructor(
    private val newsRepo: NewsRepository,
    private val newsMapper: NewsArticleMapper,
    private val isLikedArticle: Boolean
) : PagingSource<Int, NewsArticle>() {

    private var nextId: Int? = null

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        return try {
            val prevKey = if (params.key!! > 0) params.key!! - 1 else null
            val data = fetch(params.key)
            LoadResult.Page(data = data, prevKey, nextKey = nextId)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun fetch(key: Int?): List<NewsArticle> {
        val response: Result<List<NewsArticle>>
        val result: MyAPiResponse
        if (!isLikedArticle) {
            if (key == 0) {
                result = newsRepo.fetchFirstArticleBatch()
                response = newsMapper.mapList(result.Results)
            } else {
                result = nextId?.let { newsRepo.getMoreArticles(it) }!!
                response = newsMapper.mapList(result.Results)
            }
            nextId = result.NextId
        } else {
            result = newsRepo.likedArticles()
            response = newsMapper.mapList(result.Results)
        }
        return getMapperResult(response)
    }

    private fun getMapperResult(result: Result<List<NewsArticle>>): List<NewsArticle> {
        val mapperResult = result.getOrElse {
            throw it
        }
        return mapperResult
    }
}