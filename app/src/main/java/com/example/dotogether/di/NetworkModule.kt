package com.example.dotogether.di

import com.example.dotogether.BuildConfig
import com.example.dotogether.data.repostory.remote.RemoteRepository
import com.example.dotogether.data.repostory.remote.RemoteRepositoryImpl
import com.example.dotogether.util.helper.RuntimeHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRemoteRepository(retrofit: Retrofit) = retrofit.create(RemoteRepository::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor, httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideHeaderInterceptor(): Interceptor {
        val interceptor = Interceptor {
            val request = it.request().newBuilder()
                .header("Authorization", RuntimeHelper.TOKEN)
                .build()
            it.proceed(request)
        }
        return interceptor
    }

    @Singleton
    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideRemoteRepositoryImpl(remoteRepository: RemoteRepository) = RemoteRepositoryImpl(remoteRepository)
}