package com.cmt.openapp.detail.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openapp.detail.data.DetailRepository
import com.cmt.openapp.detail.data.network.response.IncidentDTODetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel() {

    private val _incidentDetail = MutableStateFlow<IncidentDTODetail?>(null)
    val incidentDetail: StateFlow<IncidentDTODetail?> = _incidentDetail

    fun loadIncidentDetail(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getIncidentDetail(id)
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _incidentDetail.value = response.body()
                    } else {
                        Log.d("DetailViewModel", "Error code: ${response.code()}")
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    Log.d("DetailViewModel", "Network error: ${it.localizedMessage}")
                }
            }
        }
    }
}