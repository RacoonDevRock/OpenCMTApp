package com.cmt.openapp.research.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmt.openapp.research.data.network.response.IncidenteDTOResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncidentePagingSource(
    private val repository: SearchRepository,
    private val fecha: String?,
    private val zona: String?,
    private val sector: String?,
    private val tipoIncidente: String?,
) : PagingSource<Int, IncidenteDTOResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IncidenteDTOResponse> {
        val page = params.key ?: 0
        return try {
            val response = repository.searchIncidents(
                fecha = fecha,
                zona = zona,
                sector = sector,
                tipoIncidente = tipoIncidente,
                page = page,
                size = params.loadSize
            )
            if (response.isSuccessful) {
                val incidents = response.body()?.embedded?.incidenteDTOPreviewList ?: emptyList()
                LoadResult.Page(
                    data = incidents,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (incidents.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, IncidenteDTOResponse>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                anchor
            )?.nextKey?.minus(1)
        }
    }
}