package nl.project.michaelmunatsi.data.repository

import com.skydoves.sandwich.*
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.network.NewsApi
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.utils.MyUtility.resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor() {
    private var apiResponse = MyAPiResponse(0, emptyList())

    suspend fun fetchFirstArticleBatch(): MyAPiResponse {
        NewsApi.retrofitService.getInitArticles()
            .onSuccess {
                apiResponse = data
            }.onFailure {
                onFailureMessage()
            }.onError {
                onErrorMessage(statusCode = statusCode.code, message = message())
            }
        return apiResponse
    }

    // get more articles  after initial batch
    suspend fun getMoreArticles(nextId: Int, numOfArticles: Int = 20): MyAPiResponse {
        NewsApi.retrofitService.getMoreArticles(nextId, numOfArticles)
            .onSuccess {
                apiResponse = data
            }.onFailure {
                onFailureMessage()
            }.onError {
                onErrorMessage(statusCode = statusCode.code, message = message())
            }
        return apiResponse
    }

    // like dislike remote api
    suspend fun likeDislikeAPi(articleId: Int, isLike: Boolean) {
        if (isLike) {
            // like article
            NewsApi.retrofitService.likeArticle(articleId)
                .onError {
                    onErrorMessage(statusCode = statusCode.code, message = message())
                }.onFailure {
                    onFailureMessage()
                }
        } else {
            // dislike article
            NewsApi.retrofitService.disLikeArticle(articleId)
                .onError {
                    onErrorMessage(statusCode = statusCode.code, message = message())
                }.onFailure {
                    onFailureMessage()
                }
        }
    }

    // fetch liked articles from api
    suspend fun likedArticles() {
        NewsApi.retrofitService.likedArticles().onSuccess {}.onError {
                onErrorMessage(statusCode = statusCode.code, message = message())
            }.onFailure {
                onFailureMessage()
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
