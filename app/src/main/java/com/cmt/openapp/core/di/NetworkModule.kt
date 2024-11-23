package com.cmt.openapp.core.di

import com.cmt.openapp.detail.data.network.DetailClient
import com.cmt.openapp.report.data.network.ReportClient
import com.cmt.openapp.research.data.network.SearchClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("http://ec2-3-139-54-49.us-east-2.compute.amazonaws.com:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
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

    @Provides
    @Singleton
    fun provideReportService(retrofit: Retrofit): ReportClient {
        return retrofit.create(ReportClient::class.java)
    }

}