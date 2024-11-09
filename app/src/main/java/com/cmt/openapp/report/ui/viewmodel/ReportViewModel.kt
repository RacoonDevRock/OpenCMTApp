package com.cmt.openapp.report.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openapp.report.data.ReportRepository
import com.cmt.openapp.report.data.network.response.SolicitudRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val repository: ReportRepository) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _idt = MutableLiveData<String>()
    val idt: LiveData<String> = _idt

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _city = MutableLiveData<String>()
    val city: LiveData<String> = _city

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _motive = MutableLiveData<String>()
    val motive: LiveData<String> = _motive

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _submissionMessage = MutableStateFlow<String?>(null)
    val submissionMessage: StateFlow<String?> = _submissionMessage

    fun solicitarAccesoIncidente(id: Long, onSuccess: () -> Unit) {
        val solicitudRequest = SolicitudRequest(
            nombreCompleto = name.value.orEmpty(),
            identificador = idt.value.orEmpty(),
            domicilio = address.value.orEmpty(),
            distrito = city.value.orEmpty(),
            correoElectronico = email.value.orEmpty(),
            telefono = phone.value.orEmpty(),
            motivo = motive.value.orEmpty()
        )

        if (validateFields(solicitudRequest)) {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.value = true
                try {
                    val response = repository.solicitarIncidente(id, solicitudRequest)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            _submissionMessage.value = "Solicitud enviada"
                            _isLoading.value = false
                            onSuccess()
                        } else {
                            _submissionMessage.value = "Error en el envío de la solicitud"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _submissionMessage.value = "Error en la red: ${e.localizedMessage}"
                    }
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            _submissionMessage.value = "Completar todos los campos"
        }
    }

    fun updateName(name: String) {
        _name.value = name
        checkFormValidity()
    }

    fun updateIdt(idt: String) {
        _idt.value = idt
        checkFormValidity()
    }

    fun updateAddress(address: String) {
        _address.value = address
        checkFormValidity()
    }

    fun updateCity(city: String) {
        _city.value = city
        checkFormValidity()
    }

    fun updateEmail(email: String) {
        _email.value = email
        checkFormValidity()
    }

    fun updatePhone(phone: String) {
        _phone.value = phone
        checkFormValidity()
    }

    fun updateMotive(motive: String) {
        _motive.value = motive
        checkFormValidity()
    }

    private fun validateFields(solicitudRequest: SolicitudRequest): Boolean {
        val isIdtValid = solicitudRequest.identificador.matches(Regex("^(\\d{8}|[a-zA-Z0-9]{1,12}|\\d{11})$"))
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(solicitudRequest.correoElectronico).matches()
        val isPhoneValid = solicitudRequest.telefono.matches(Regex("^(9\\d{8}|\\d{7,8})$"))

        return isIdtValid &&
                isEmailValid &&
                isPhoneValid &&
                solicitudRequest.nombreCompleto.isNotBlank() &&
                solicitudRequest.domicilio.isNotBlank() &&
                solicitudRequest.distrito.isNotBlank() &&
                solicitudRequest.motivo.isNotBlank()
    }

    private fun checkFormValidity() {
        _isFormValid.value = _name.value.orEmpty().isNotBlank() &&
                _idt.value.orEmpty().isNotBlank() &&
                _address.value.orEmpty().isNotBlank() &&
                _city.value.orEmpty().isNotBlank() &&
                _email.value.orEmpty().isNotBlank() &&
                _phone.value.orEmpty().isNotBlank() &&
                _motive.value.orEmpty().isNotBlank()
    }

    fun resetNavigation() {
        _submissionMessage.value = null
    }

}