package nl.project.michaelmunatsi.data.remote

import com.skydoves.sandwich.ApiResponse
import nl.project.michaelmunatsi.model.MyAPiResponse
import okhttp3.ResponseBody
import retrofit2.http.*

interface NewsApiService {
    @GET("Articles")
    suspend fun getInitArticles(
        @Header("x-authtoken") token: String?
    ): MyAPiResponse

    @GET("Articles/{id}")
    suspend fun getMoreArticles(
        @Path("id") nextId: Int? = null,
        @Header("x-authtoken") token: String?,
        @Query("count") count: Int = 20,
    ): MyAPiResponse

    @PUT("Articles/{id}/like")
    suspend fun likeArticle(
        @Path("id") id: Int,
        @Header("x-authtoken") token: String?,
    ): ApiResponse<ResponseBody>

    @DELETE("Articles/{id}/like")
    suspend fun disLikeArticle(
        @Path("id") id: Int,
        @Header("x-authtoken") token: String?,
    ): ApiResponse<ResponseBody>
}