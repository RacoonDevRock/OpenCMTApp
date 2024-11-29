package com.cmt.openapp.report.data.network

import com.cmt.openapp.report.data.network.response.SolicitudRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportClient {

    @POST("api/incidente/{id}/solicitar")
    suspend fun solicitarIncidente(
        @Path("id") id: Int,
        @Body solicitudRequest: SolicitudRequest,
    ): Response<Unit>
}