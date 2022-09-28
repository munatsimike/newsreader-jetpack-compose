package nl.project.michaelmunatsi.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import javax.inject.Inject

class NewsArticlePager @Inject constructor(
    private val newsRepo: NewsRepository, private val newsMapper: NewsArticleMapper
) : PagingSource<Int, NewsArticle>() {

    private var nextId: Int? = null

    override val keyReuseSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        return try {
            val data = fetch(params.key)
            LoadResult.Page(data = data, null, nextKey = nextId)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun fetch(key: Int?): List<NewsArticle> {
        val response: Result<List<NewsArticle>>
        val result: MyAPiResponse
        if (key == 0) {
            result = newsRepo.fetchFirstArticleBatch()
            response = newsMapper.mapList(result.Results)
        } else {
            result = nextId?.let { newsRepo.getMoreArticles(it) }!!
            response = newsMapper.mapList(result.Results)
        }
        nextId = result.NextId
        return getMapperResult(response)
    }

    private fun getMapperResult(result: Result<List<NewsArticle>>): List<NewsArticle> {
        val mapperResult = result.getOrElse {
            throw it
        }
        return mapperResult
    }
}