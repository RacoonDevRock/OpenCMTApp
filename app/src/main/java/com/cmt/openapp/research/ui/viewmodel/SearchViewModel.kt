package com.cmt.openapp.research.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.cmt.openapp.research.data.IncidentePagingSource
import com.cmt.openapp.research.data.SearchRepository
import com.cmt.openapp.research.data.network.response.Filters
import com.cmt.openapp.research.data.network.response.SectorDTO
import com.cmt.openapp.research.data.network.response.TipoIncidenteDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
) : ViewModel() {

    var date by mutableStateOf<String?>(null)
    var zone by mutableStateOf<String?>(null)
    var sect by mutableStateOf<String?>(null)
    var accidentType by mutableStateOf<String?>(null)

    private val _sectores = MutableStateFlow<List<SectorDTO>>(emptyList())
    val sectores: StateFlow<List<SectorDTO>> = _sectores

    private val filters = MutableStateFlow(Filters())

    private val _tiposDeIncidente = MutableStateFlow<List<TipoIncidenteDTO>>(emptyList())
    val tiposDeIncidente: StateFlow<List<TipoIncidenteDTO>> = _tiposDeIncidente

    fun obtenerIncidentesPaginated() = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            IncidentePagingSource(
                repository = repository,
                fecha = date,
                zona = zone,
                sector = sect,
                tipoIncidente = accidentType
            )
        }
    ).flow.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val incidentsFlow = filters.flatMapLatest { filter ->
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                IncidentePagingSource(
                    repository = repository,
                    fecha = filter.date,
                    zona = filter.zone,
                    sector = filter.sect,
                    tipoIncidente = filter.accidentType
                )
            }
        ).flow
    }.cachedIn(viewModelScope)

    fun updateFilters(date: String?, zone: String?, sect: String?, accidentType: String?) {
        filters.value = Filters(date, zone, sect, accidentType)
    }

    fun obtenerSectoresPorZona(zona: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SearchViewModel", "Obteniendo sectores para zona: $zona")
            val response = repository.obtenerSectoresPorZona(zona)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _sectores.value = response.body() ?: emptyList()
                    Log.d("SearchViewModel", "Sectores obtenidos: ${_sectores.value}")
                }
            }
        }
    }

    fun obtenerTiposDeIncidente() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.obtenerTiposDeIncidente()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _tiposDeIncidente.value = response.body() ?: emptyList()
                    Log.d("SearchViewModel", "Tipos de incidente obtenidos: ${_tiposDeIncidente.value}")
                }
            } else {
                Log.e("SearchViewModel", "Error al obtener tipos de incidente: ${response.code()}")
            }
        }
    }

    fun updateSector(sector: String?) {
        sect = sector
        if (zone == null) _sectores.value = emptyList()
    }
}