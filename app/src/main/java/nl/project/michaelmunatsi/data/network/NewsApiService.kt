package nl.project.michaelmunatsi.data.network

import com.skydoves.sandwich.ApiResponse
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.Token
import okhttp3.ResponseBody
import retrofit2.http.*

private var myToken: String = ""

fun updateHeaderToken(token: Token?) {
    myToken = token?.AuthToken ?: ""
}
interface NewsApiService {
    @GET("Articles")
    suspend fun getInitArticles(): ApiResponse<MyAPiResponse>

    @GET("Articles/{id}")
    suspend fun getMoreArticles(
        @Path("id") nextId: Int,
        @Query("count") count: Int,
    ): ApiResponse<MyAPiResponse>

    @GET("Articles/liked")
    suspend fun likedArticles(@Header("x-authtoken") token: String? = myToken): ApiResponse<MyAPiResponse>

    @PUT("Articles/{id}/like")
    suspend fun likeArticle(
        @Path("id") id: Int,
        @Header("x-authtoken") token: String? = myToken,
    ): ApiResponse<ResponseBody>

    @DELETE("Articles/{id}/like")
    suspend fun disLikeArticle(
        @Path("id") id: Int,
        @Header("x-authtoken") token: String? = myToken,
    ): ApiResponse<ResponseBody>
}