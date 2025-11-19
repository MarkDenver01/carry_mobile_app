package com.nathaniel.carryapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.data.remote.api.PsgcApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://carrybackend-dfyh.onrender.com"
    private const val LOCATION_BASE_URL = "https://psgc.gitlab.io"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocationApi

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(tokenManager: TokenManager): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
                .addHeader("Content-Type", "application/json")

            val path = request.url.encodedPath

            // Skip Authorization only for this specific endpoint
            val shouldSkipAuth = path.startsWith("/user/public/")

            // updated token
            val token = tokenManager.getToken()

            if (!shouldSkipAuth && !token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

    @Provides
    @Singleton
    @MainApi
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @LocationApi
    fun provideLocationRetrofit(
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        @MainApi retrofit: Retrofit
    ): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providePsgcApiService(
        @LocationApi retrofit: Retrofit
    ): PsgcApiService =
        retrofit.create(PsgcApiService::class.java)

}