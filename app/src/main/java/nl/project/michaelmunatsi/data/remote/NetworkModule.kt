package nl.project.michaelmunatsi.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.project.michaelmunatsi.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://inhollandbackend.azurewebsites.net/api/"

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideLogger(): OkHttpClient = OkHttpClient.Builder().also { client ->
        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(logger)
        }

    }.build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL).client(okHttpClient).build()

    @Provides
    fun provideNetworkService(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
}