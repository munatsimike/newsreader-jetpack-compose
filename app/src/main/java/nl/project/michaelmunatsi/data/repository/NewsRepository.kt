package nl.project.michaelmunatsi.data.repository

import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.data.network.NewsApi
import javax.inject.Inject

class NewsRepository @Inject constructor(){
    suspend fun fetchFirstArticleBatch(): MyAPiResponse {
        var apiResponse = MyAPiResponse(0, emptyList())
        NewsApi.retrofitService.getInitArticles().onSuccess {
            if (data.Results.isNotEmpty() && data.NextId != 0) apiResponse = data
        }.onFailure {
            throw IllegalStateException("No internet access")
        }.onError {
            if (statusCode.code == 401) {
                throw IllegalStateException("User not logged in, please login to save and access favourites")
            }
        }
        return apiResponse
    }
}