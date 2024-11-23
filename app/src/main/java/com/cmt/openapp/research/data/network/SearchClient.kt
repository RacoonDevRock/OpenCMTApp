package com.cmt.openapp.research.data.network

import com.cmt.openapp.research.data.network.response.IncidentResponse
import com.cmt.openapp.research.data.network.response.SectorDTO
import com.cmt.openapp.research.data.network.response.TipoIncidenteDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchClient {

    @GET("api/incidente/buscar")
    suspend fun searchIncidents(
        @Query("fecha") fecha: String? = null,
        @Query("zona") zona: String? = null,
        @Query("sector") sector: String? = null,
        @Query("tipoIncidente") tipoIncidente: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<IncidentResponse>

    @GET("api/incidente")
    suspend fun obtenerSectoresPorZona(
        @Query("zona") zona: String
    ): Response<List<SectorDTO>>

    @GET("api/incidente/tipo-incidente")
    suspend fun obtenerIncidentes(): Response<List<TipoIncidenteDTO>>
}