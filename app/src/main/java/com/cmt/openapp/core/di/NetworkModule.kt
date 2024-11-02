package com.cmt.openapp.core.di

import com.cmt.openapp.detail.data.network.DetailClient
import com.cmt.openapp.research.data.network.SearchClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchClient {
        return retrofit.create(SearchClient::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailService(retrofit: Retrofit): DetailClient {
        return retrofit.create(DetailClient::class.java)
    }

}