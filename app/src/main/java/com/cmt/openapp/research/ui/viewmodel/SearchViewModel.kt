package com.cmt.openapp.research.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openapp.research.data.SearchRepository
import com.cmt.openapp.research.data.network.response.IncidenteDTOResponse
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

    var date by mutableStateOf("")
    var zone by mutableStateOf("")
    var sect by mutableStateOf("")
    var accidentType by mutableStateOf("")

    init {
        loadAllIncidents()
    }

    fun loadAllIncidents() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching {
                repository.getAllIncidents()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _uiState.value = IncidentUIState(incidents = response.body() ?: emptyList())
                } else {
                    _uiState.value =
                        _uiState.value.copy(errorMessage = "Error al cargar los incidentes")
                    Log.d("ResponseStatus", "Error code: ${response.code()}")
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = "Error de red")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun searchIncidents(
        fecha: String? = date,
        zona: String? = zone,
        sector: String? = sect,
        tipoIncidente: String? = accidentType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching {
                repository.searchIncidents(fecha, zona, sector, tipoIncidente)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _uiState.value = IncidentUIState(incidents = response.body() ?: emptyList())
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = "Error en la búsqueda")
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = "Error de red")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}