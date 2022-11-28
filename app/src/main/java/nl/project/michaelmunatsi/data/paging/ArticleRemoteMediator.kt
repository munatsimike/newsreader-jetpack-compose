package nl.project.michaelmunatsi.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import nl.project.michaelmunatsi.data.database.ArticleDB
import nl.project.michaelmunatsi.data.remote.NewsApi
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import nl.project.michaelmunatsi.model.RemoteKey

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val dataBase: ArticleDB,
    private val newsArticleMapper: NewsArticleMapper,
    private val newsRepository: NewsRepository
) : RemoteMediator<Int, NewsArticle>() {

    private val articleDao = dataBase.newsDao
    private val remoteKeyDao = dataBase.remoteKeyDao

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, NewsArticle>
    ): MediatorResult {
        return try {
            val remoteKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = dataBase.withTransaction {
                        remoteKeyDao.getRemoteKey()
                    }
                    if (remoteKey.nextId == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextId
                }
            }
            val result = if (remoteKey == null) {
                NewsApi.retrofitService.getInitArticles(token = getToken())
            } else {
                NewsApi.retrofitService.getMoreArticles(remoteKey, token = getToken())
            }

            if (loadType == LoadType.REFRESH) {
                dataBase.withTransaction {
                    remoteKeyDao.deleteAllRemoteKeys()
                    articleDao.deleteAllArticles()
                }
            }
            handleApiResponse(result)
            MediatorResult.Success(endOfPaginationReached = result.Results.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun handleApiResponse(result: MyAPiResponse) {
        dataBase.withTransaction {
            val nextPage = if (result.Results.isEmpty()) {
                null
            } else {
                result.NextId
            }
            val articles: List<NewsArticle> =
                getMapperResult(newsArticleMapper.mapList(result.Results))
            // save remote keys to local db
            remoteKeyDao.insertOrReplace(RemoteKey(nextPage))
            // save articles to room
            articleDao.insertAll(article = articles)
        }
    }

    private suspend fun getToken(): String? {
        var token: String? = null
        newsRepository.authToken.take(1).collectLatest {
            it?.let {
                token = it.AuthToken
            }
        }
        return token
    }

    // validate API response using news article mapper
    private fun getMapperResult(result: Result<List<NewsArticle>>): List<NewsArticle> {
        val mapperResult = result.getOrElse {
            throw it
        }
        return mapperResult
    }
}
