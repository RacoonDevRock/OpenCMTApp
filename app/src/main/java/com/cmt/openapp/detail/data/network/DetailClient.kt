package com.cmt.openapp.detail.data.network

import com.cmt.openapp.detail.data.network.response.IncidentDTODetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailClient {

    @GET("api/incidente/{id}")
    suspend fun getIncidentDetail(@Path("id") id: Long): Response<IncidentDTODetail>
}