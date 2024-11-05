package com.cmt.openapp.report.data

import com.cmt.openapp.report.data.network.ReportClient
import com.cmt.openapp.report.data.network.response.SolicitudRequest
import retrofit2.Response
import javax.inject.Inject

class ReportRepository @Inject constructor(private val reportClient: ReportClient) {
    suspend fun solicitarIncidente(id: Long, solicitudRequest: SolicitudRequest): Response<Unit> {
        return reportClient.solicitarIncidente(id, solicitudRequest)
    }
}