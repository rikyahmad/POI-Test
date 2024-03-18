package com.staygrateful.poi_test.di

import com.google.gson.GsonBuilder
import com.staygrateful.poi_test.BuildConfig
import com.staygrateful.poi_test.data.api.ApiService
import com.staygrateful.poi_test.data.repository.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {

    @Provides
    @Named("base_url")
    open fun provideBaseUrl() = BuildConfig.API_BASE_URL

    @Provides
    fun provideRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                val req = chain.request().newBuilder().apply {
                    this.addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
                    this.addHeader("X-RapidAPI-Host", BuildConfig.API_HOST)
                }
                chain.proceed(req.build())
            } catch (e: Exception) {
                e.printStackTrace()
                chain.proceed(chain.request())
            }
        }
    }

    @Provides
    fun provideOkhttpClient(interceptor: Interceptor): OkHttpClient {
        val networkTimeout: Long = 30

        val logger = HttpLoggingInterceptor.Logger { message ->
            Platform.get().log(message)
        }

        val httpClientBuilder = OkHttpClient.Builder().apply {
            connectTimeout(networkTimeout, TimeUnit.SECONDS)
            writeTimeout(networkTimeout, TimeUnit.SECONDS)
            readTimeout(networkTimeout, TimeUnit.SECONDS)
            addInterceptor(interceptor)
            addInterceptor(HttpLoggingInterceptor(logger).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return httpClientBuilder.build()
    }

    @Provides
    fun provideRestClient(
        okHttpClient: OkHttpClient,
        @Named("base_url") baseUrl: String
    ): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideApiServices(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    internal fun provideRemoteDataSource(
        mService: ApiService,
    ): RemoteDataSource = RemoteDataSource(mService)
}