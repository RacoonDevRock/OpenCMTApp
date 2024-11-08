package com.cmt.openapp.report.ui.viewmodel

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

    private val _errors = MutableLiveData<Map<String, String?>>()
    val errors: LiveData<Map<String, String?>> = _errors

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
                            onSuccess()
                        } else {
                            _submissionMessage.value = "Error en el envío de la solicitud"                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _submissionMessage.value = "Error en la red: ${e.localizedMessage}"
                    }
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateIdt(idt: String) {
        _idt.value = idt
    }

    fun updateAddress(address: String) {
        _address.value = address
    }

    fun updateCity(city: String) {
        _city.value = city
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePhone(phone: String) {
        _phone.value = phone
    }

    fun updateMotive(motive: String) {
        _motive.value = motive
    }

    private fun validateFields(solicitudRequest: SolicitudRequest): Boolean {
        val errorsMap = mutableMapOf<String, String?>()

        errorsMap["nombreCompleto"] = if (solicitudRequest.nombreCompleto.isBlank()) {
            "Nombre completo es requerido"
        } else null

        errorsMap["identificador"] =
            if (!solicitudRequest.identificador.matches(Regex("^(\\d{8}|[a-zA-Z0-9]{1,12}|\\d{11})$"))) {
                "Identificador debe ser DNI (8 dígitos), CE (hasta 12 caracteres) o RUC (11 dígitos)"
            } else null

        errorsMap["correoElectronico"] =
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(solicitudRequest.correoElectronico)
                    .matches()
            ) {
                "Correo electrónico inválido"
            } else null

        errorsMap["telefono"] =
            if (!solicitudRequest.telefono.matches(Regex("^(9\\d{8}|\\d{7,8})$"))) {
                "Teléfono debe ser un número móvil de 9 dígitos o fijo de 7-8 dígitos"
            } else null

        _errors.value = errorsMap

        return errorsMap.values.all { it == null }
    }

    fun resetNavigation() {
        _submissionMessage.value = null
    }

}