package nl.project.michaelmunatsi.data.remote

import com.skydoves.sandwich.ApiResponse
import nl.project.michaelmunatsi.model.MyAPiResponse
import nl.project.michaelmunatsi.model.Token
import okhttp3.ResponseBody
import retrofit2.http.*

private var myToken: String? = null

fun updateHeaderToken(token: Token?) {
    myToken = token?.AuthToken
}

interface NewsApiService {
    @GET("Articles")
    suspend fun getInitArticles(
        @Header("x-authtoken") token: String? = myToken,
        @Query("count") count: Int = 50
    ): MyAPiResponse

    @GET("Articles/{id}")
    suspend fun getMoreArticles(
        @Path("id") nextId: Int,
        @Query("count") count: Int = 50,
    ): MyAPiResponse

    @GET("Articles/liked")
    suspend fun likedArticles(@Header("x-authtoken") token: String? = myToken): MyAPiResponse

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