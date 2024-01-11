package com.example.dogtypes.di

import com.example.dogtypes.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(com.example.dogtypes.api.ApiService::class.java)

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ) = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

    @Provides
    fun provideHttpClient() = OkHttpClient
        .Builder()
        .addInterceptor(com.example.dogtypes.api.ApiServiceInterceptor)
        .build()
}