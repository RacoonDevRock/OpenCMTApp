package com.cmt.openapp.research.data

import com.cmt.openapp.research.data.network.SearchClient
import com.cmt.openapp.research.data.network.response.SectorDTO
import com.cmt.openapp.research.data.network.response.TipoIncidenteDTO
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchClient: SearchClient) {

    suspend fun searchIncidents(
        fecha: String?,
        zona: String?,
        sector: String?,
        tipoIncidente: String?,
        page: Int,
        size: Int
    ) = searchClient.searchIncidents(fecha, zona, sector, tipoIncidente, page, size)

    suspend fun obtenerSectoresPorZona(zona: String): Response<List<SectorDTO>> {
        return searchClient.obtenerSectoresPorZona(zona)
    }

    suspend fun obtenerTiposDeIncidente() : Response<List<TipoIncidenteDTO>> {
        return searchClient.obtenerIncidentes()
    }
}