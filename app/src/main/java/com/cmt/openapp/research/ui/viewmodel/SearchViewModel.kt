package com.cmt.openapp.research.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openapp.research.data.SearchRepository
import com.cmt.openapp.research.data.network.response.IncidenteDTOResponse
import com.cmt.openapp.research.data.network.response.SectorDTO
import com.cmt.openapp.research.data.network.response.TipoIncidenteDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class IncidentUIState(
    val incidents: List<IncidenteDTOResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(IncidentUIState())
    val uiState: StateFlow<IncidentUIState> = _uiState

    var date by mutableStateOf<String?>(null)
    var zone by mutableStateOf<String?>(null)
    var sect by mutableStateOf<String?>(null)
    var accidentType by mutableStateOf<String?>(null)

    private val _sectores = MutableStateFlow<List<SectorDTO>>(emptyList())
    val sectores: StateFlow<List<SectorDTO>> = _sectores

    private val _tiposDeIncidente = MutableStateFlow<List<TipoIncidenteDTO>>(emptyList())
    val tiposDeIncidente: StateFlow<List<TipoIncidenteDTO>> = _tiposDeIncidente

    private var currentPage = 0
    private var isEndReached = false
    private val pageSize = 10

    init {
        searchIncidents()
    }

    fun searchIncidents() {
        // Reiniciar la paginación si se inicia una nueva búsqueda
        currentPage = 0
        isEndReached = false
        _uiState.value = _uiState.value.copy(incidents = emptyList())  // Limpiar la lista
        loadNextPage()  // Cargar la primera página
    }

    fun refreshIncidents() {
        // Refrescar incidentes reiniciando el estado
        currentPage = 0
        isEndReached = false
        _uiState.value = _uiState.value.copy(incidents = emptyList(), isLoading = true)
        loadNextPage()
    }

    fun loadNextPage() {
        // Verificar si se alcanzó el final de las páginas
        if (isEndReached) return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching {
                repository.searchIncidents(date, zone, sect, accidentType, currentPage, pageSize) // Modificar el repositorio para soportar la paginación
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val newIncidents = response.body() ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        incidents = _uiState.value.incidents + newIncidents, // Agregar incidentes
                        isLoading = false
                    )
                    currentPage++

                    // Verificar si ya no hay más páginas
                    if (newIncidents.size < pageSize) {
                        isEndReached = true
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error en la búsqueda",
                        isLoading = false
                    )
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error de red",
                    isLoading = false
                )
            }
        }
    }

    fun obtenerSectoresPorZona(zona: String) {
        _sectores.value = emptyList() // Limpia la lista antes de cargar nuevos sectores
        viewModelScope.launch {
            val response = repository.obtenerSectoresPorZona(zona)
            if (response.isSuccessful) {
                _sectores.value = response.body() ?: emptyList()
            } else {
                // Manejar el error según tu lógica de negocio
                _uiState.value = _uiState.value.copy(errorMessage = "Error al cargar sectores")
            }
        }
    }

    fun obtenerTiposDeIncidente() {
        viewModelScope.launch {
            val response = repository.obtenerTiposDeIncidente()
            if (response.isSuccessful) {
                _tiposDeIncidente.value = response.body() ?: emptyList()
            } else {
                _uiState.value = _uiState.value.copy(errorMessage = "Error al cargar tipos de incidente")
            }
            _uiState.value = _uiState.value.copy(isLoading = false) // Detén el indicador de carga
        }
    }
}