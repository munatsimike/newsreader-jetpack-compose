package nl.project.michaelmunatsi.data.network

import com.skydoves.sandwich.ApiResponse
import nl.project.michaelmunatsi.model.MyAPiResponse
import retrofit2.http.*

interface NewsApiService {
    @GET("Articles")
    suspend fun getInitArticles(): ApiResponse<MyAPiResponse>

    @GET("Articles/{id}")
    suspend fun getMoreArticlesAsync(
        @Path("id") nextId: Int,
        @Query("count") count: Int,
    ): ApiResponse<MyAPiResponse>
}