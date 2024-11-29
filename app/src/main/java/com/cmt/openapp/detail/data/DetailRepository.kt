package com.cmt.openapp.detail.data

import com.cmt.openapp.detail.data.network.DetailClient
import com.cmt.openapp.detail.data.network.response.IncidentDTODetail
import retrofit2.Response
import javax.inject.Inject

class DetailRepository @Inject constructor(private val detailClient: DetailClient) {
    suspend fun getIncidentDetail(id: Int): Response<IncidentDTODetail> {
        return detailClient.getIncidentDetail(id)
    }
}