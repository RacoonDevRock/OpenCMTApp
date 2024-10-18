package com.cmt.openapp.research.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    var date by mutableStateOf("")
    var zone by mutableStateOf("")
    var sector by mutableStateOf("")
    var accidentType by mutableStateOf("")
}