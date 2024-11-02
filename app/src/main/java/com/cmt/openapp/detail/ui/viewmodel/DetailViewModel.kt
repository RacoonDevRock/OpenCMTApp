package com.cmt.openapp.detail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openapp.detail.data.DetailRepository
import com.cmt.openapp.detail.data.network.response.IncidentDTODetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel() {

    private val _incidentDetail = MutableStateFlow<IncidentDTODetail?>(null)
    val incidentDetail: StateFlow<IncidentDTODetail?> = _incidentDetail

    fun loadIncidentDetail(id: Long) {
        viewModelScope.launch {
            _incidentDetail.value = repository.getIncidentDetail(id)
        }
    }
}