package com.cmt.openapp.research.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openapp.research.data.SearchRepository
import com.cmt.openapp.research.data.network.response.IncidenteDTOResponse
import com.cmt.openapp.research.data.network.response.SectorDTO
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

    init {
        searchIncidents()
    }

    fun searchIncidents() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching {
                repository.searchIncidents(date, zone, sect, accidentType)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _uiState.value = IncidentUIState(response.body() ?: emptyList())
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = "Error en la búsqueda")
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = "Error de red")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
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
}