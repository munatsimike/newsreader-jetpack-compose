package nl.project.michaelmunatsi.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import kotlinx.coroutines.flow.Flow
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.database.ArticleDB
import nl.project.michaelmunatsi.data.paging.ArticleRemoteMediator
import nl.project.michaelmunatsi.data.paging.LikedArticlePager
import nl.project.michaelmunatsi.data.remote.NewsApi
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.utils.MyUtility.toInt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val database: ArticleDB,
    private val newsMapper: NewsArticleMapper
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllArticles(): Flow<PagingData<NewsArticle>> {
        val pagingSourceFactory = { database.newsDao.getAllArticles() }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = ArticleRemoteMediator(
                dataBase = database,
                newsMapper
            ), pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    // like dislike remote api
    suspend fun likeDislikeAPi(articleId: Int, isLike: Boolean) {
        if (isLike) {
            // like article
            NewsApi.retrofitService.likeArticle(articleId).onError {
                onErrorMessage(statusCode = statusCode.code, message = message())
            }.onFailure {
                onFailureMessage()
            }
        } else {
            // dislike article
            NewsApi.retrofitService.disLikeArticle(articleId).onError {
                onErrorMessage(statusCode = statusCode.code, message = message())
            }.onFailure {
                onFailureMessage()
            }
        }
        likeDislikeRoomDB(isLike, articleId)
    }

    // fetch liked articles from api
    fun likedArticles(): Flow<PagingData<NewsArticle>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                LikedArticlePager(newsMapper)
            }
        ).flow
    }

    // like dislike article in local database
    private suspend fun likeDislikeRoomDB(isLike: Boolean, id: Int) {
        if (isLike) {
            // like article
            database.newsDao.likeDislike(isLike.toInt(), id)
        } else {
            // dislike article
            database.newsDao.likeDislike(isLike.toInt(), id)
        }

    }

    private fun onFailureMessage() {
        throw IllegalStateException(resource.getString(R.string.No_internet_access))
    }

    private fun onErrorMessage(statusCode: Int, message: String) {
        if (statusCode == 401) {
            throw IllegalStateException(resource.getString(R.string.user_not_logged_in))
        } else {
            throw IllegalStateException(message)
        }
    }
}
