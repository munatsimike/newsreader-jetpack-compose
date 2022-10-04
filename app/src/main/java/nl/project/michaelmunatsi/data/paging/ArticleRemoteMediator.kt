package nl.project.michaelmunatsi.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import nl.project.michaelmunatsi.data.database.ArticleDB
import nl.project.michaelmunatsi.data.remote.NewsApi
import nl.project.michaelmunatsi.model.*
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val dataBase: ArticleDB,
    private val newsArticleMapper: NewsArticleMapper
) : RemoteMediator<Int, NewsArticle>() {

    private val articleDao = dataBase.newsDao
    private val remoteKeysDao = dataBase.RemoteKeysDao
    private var articles: List<NewsArticleEntity> = emptyList()
    private var nextId: Int = 0

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, NewsArticle>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }
            articles = if (state.isEmpty()) {
                getArticles(NewsApi.retrofitService.getInitArticles())
            } else {
                getArticles(NewsApi.retrofitService.getMoreArticles(nextId!!))
            }

            val articles: List<NewsArticle> = getMapperResult(newsArticleMapper.mapList(articles))

            val endOfPaginationReached = (nextId==0)

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            dataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    articleDao.deleteAllArticles()
                    remoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = articles.map { article ->
                    ArticleRemoteKeys(
                        id = article.Id, prevPage = prevPage, nextPage = nextPage
                    )
                }
                remoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                articleDao.insertAll(article = articles)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private fun getCurrentPage(){

    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, NewsArticle>
    ): ArticleRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.Id?.let { id ->
                remoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, NewsArticle>
    ): ArticleRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                remoteKeysDao.getRemoteKeys(id = article.Id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, NewsArticle>
    ): ArticleRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article ->
                remoteKeysDao.getRemoteKeys(id = article.Id)
            }
    }

    private fun getMapperResult(result: Result<List<NewsArticle>>): List<NewsArticle> {
        val mapperResult = result.getOrElse {
            throw it
        }
        return mapperResult
    }

    private fun getArticles(response: MyAPiResponse): List<NewsArticleEntity> {
        nextId = response.NextId
        return response.Results
    }
}
