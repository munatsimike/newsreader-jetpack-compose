package nl.project.michaelmunatsi.data.remote

import com.skydoves.sandwich.ApiResponse
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.UserRegistrationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {
    @FormUrlEncoded
    @POST("Users/login")
    suspend fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<Token>

    @FormUrlEncoded
    @POST("Users/register")
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<UserRegistrationResponse>
}