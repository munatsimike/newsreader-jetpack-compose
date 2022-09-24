package nl.project.michaelmunatsi.data.network

import com.skydoves.sandwich.ApiResponse
import nl.project.michaelmunatsi.model.MyAPiResponse
import retrofit2.http.GET

interface NewsApiService {
    @GET("Articles")
    suspend fun getInitArticles(): ApiResponse<MyAPiResponse>
}



