package nl.project.michaelmunatsi.data.remote

import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://inhollandbackend.azurewebsites.net/api/"

private val moshi = Moshi.Builder() // adapter
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit by lazy {
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}

object NewsApi {
    val retrofitService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}

object UserApi {
    val retrofitService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}