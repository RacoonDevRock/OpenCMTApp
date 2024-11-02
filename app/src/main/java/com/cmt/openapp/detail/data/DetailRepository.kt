package com.cmt.openapp.detail.data

import com.cmt.openapp.detail.data.network.DetailClient
import com.cmt.openapp.detail.data.network.response.IncidentDTODetail
import javax.inject.Inject

class DetailRepository @Inject constructor(private val detailClient: DetailClient) {
    suspend fun getIncidentDetail(id: Long): IncidentDTODetail {
        return detailClient.getIncidentDetail(id)
    }
}