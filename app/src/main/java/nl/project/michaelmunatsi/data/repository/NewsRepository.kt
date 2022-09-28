package nl.project.michaelmunatsi.data.repository

import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.network.NewsApi
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.utils.MyUtility.resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor() {
    private var apiResponse = MyAPiResponse(0, emptyList())

    suspend fun fetchFirstArticleBatch(): MyAPiResponse {
        NewsApi.retrofitService.getInitArticles().onSuccess {
            if (data.Results.isNotEmpty() && data.NextId != 0)
                apiResponse = data
        }.onFailure {
            onFailureMessage()
        }.onError {
            onErrorMessage(statusCode = statusCode.code, message = message())
        }
        onFailureMessage()
        return apiResponse
    }

    // get more articles  after initial batch
    suspend fun getMoreArticles(nextId: Int, numOfArticles: Int = 20): MyAPiResponse {
        NewsApi.retrofitService.getMoreArticles(nextId, numOfArticles)
            .onSuccess {
                if (data.Results.isNotEmpty() && data.NextId != 0) apiResponse = data
            }.onFailure {
                onFailureMessage()
            }.onError {
                onErrorMessage(statusCode = statusCode.code, message = message())
            }
        return apiResponse
    }

    // like dislike remote api
    suspend fun likeDislikeAPi(article: NewsArticle) {
        if (!article.IsLiked) {
            // like article
            NewsApi.retrofitService.likeArticle(article.Id)
                .onSuccess {

                }.onError {
                    onErrorMessage(statusCode = statusCode.code, message = message())
                }.onFailure {}
        } else {
            // dislike article
            NewsApi.retrofitService.disLikeArticle(article.Id)
                .onSuccess {}
                .onError {
                    onErrorMessage(statusCode = statusCode.code, message = message())
                }.onFailure {}
        }
    }

    // fetch liked articles from api
    suspend fun likedArticles() {
        NewsApi.retrofitService.likedArticles()
            .onSuccess {

            }.onError {
                onErrorMessage(statusCode = statusCode.code, message = message())
            }.onFailure {}
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
