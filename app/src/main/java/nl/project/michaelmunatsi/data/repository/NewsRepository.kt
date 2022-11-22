package nl.project.michaelmunatsi.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow
import nl.project.michaelmunatsi.data.database.ArticleDB
import nl.project.michaelmunatsi.data.paging.ArticleRemoteMediator
import nl.project.michaelmunatsi.data.remote.NewsApi
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.utils.MyUtility.toInt
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

// contains code fetching articles from room and remote api
@Singleton
class NewsRepository @Inject constructor(
    private val database: ArticleDB,
    private val newsMapper: NewsArticleMapper,

    userManager: UserManager
) : BaseRepository(userManager) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllArticles(): Flow<PagingData<NewsArticle>> {
        return Pager(config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = ArticleRemoteMediator(
                dataBase = database,
                newsArticleMapper = newsMapper,
                this
            ),
            pagingSourceFactory = { database.newsDao.getAllArticles() }).flow
    }

    suspend fun getArticle(id: Int): Flow<NewsArticle> = database.newsDao.getArticle(id)

    // like dislike remote api
    suspend fun likeDislikeAPi(
        articleId: Int,
        isLike: Boolean,
        token: Token
    ): ApiResponse<ResponseBody> {
        if (isLike) {
            // like article
            return NewsApi.retrofitService.likeArticle(articleId, token.AuthToken)
        }
        // invalidate paging source
        // dislike article
        return NewsApi.retrofitService.disLikeArticle(articleId, token.AuthToken)
    }

    // fetch liked articles from api
    suspend fun likedArticles(token: Token): ApiResponse<MyAPiResponse> {
        return NewsApi.retrofitService.likedArticles(token = token.AuthToken)
    }

    // like dislike article in local database
    suspend fun likeDislikeRoomDB(isLike: Boolean, id: Int) {
        if (isLike) {
            // like article
            database.newsDao.likeDislike(isLike.toInt(), id)
        } else {
            // dislike article
            database.newsDao.likeDislike(isLike.toInt(), id)
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}
