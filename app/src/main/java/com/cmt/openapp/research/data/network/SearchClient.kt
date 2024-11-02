package com.cmt.openapp.research.data.network

import com.cmt.openapp.research.data.network.response.IncidentResponse
import com.cmt.openapp.research.data.network.response.IncidenteDTOResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchClient {

    @GET("api/incidente/todos")
    suspend fun getAllIncidents(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<IncidentResponse>

    @GET("api/incidente/buscar")
    suspend fun searchIncidents(
        @Query("fecha") fecha: String?,
        @Query("zona") zona: String?,
        @Query("sector") sector: String?,
        @Query("tipoIncidente") tipoIncidente: String?,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<IncidentResponse>
}