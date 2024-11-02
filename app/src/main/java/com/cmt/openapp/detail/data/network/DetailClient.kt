package com.cmt.openapp.detail.data.network

import com.cmt.openapp.detail.data.network.response.IncidentDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailClient {

    @GET("api/incidente/{id}")
    suspend fun getIncidentById(
        @Path("id") id: Long
    ): Response<IncidentDTO>
}