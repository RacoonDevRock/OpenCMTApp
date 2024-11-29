package com.cmt.openapp.report.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.report.data.ReportRepository
import com.cmt.openapp.report.data.network.response.FormData
import com.cmt.openapp.report.data.network.response.FormValidationResult
import com.cmt.openapp.report.data.network.response.SolicitudRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val repository: ReportRepository) : ViewModel() {

    private val _formData = MutableStateFlow(FormData())
    val formData: StateFlow<FormData> = _formData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _submissionMessage = MutableStateFlow<String?>(null)
    val submissionMessage: StateFlow<String?> = _submissionMessage

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors

    fun handleSubmissionResult(navigationController: NavHostController) {
        viewModelScope.launch {
            submissionMessage.collectLatest { message ->
                message?.let {
                    if (it == "Solicitud enviada") {
                        withContext(Dispatchers.Main) {
                            navigationController.navigate(Routes.ResearchScreen.route) {
                                popUpTo(Routes.ReportScreen.route) { inclusive = true }
                            }
                        }
                    }
                    resetNavigation()
                }
            }
        }
    }

    fun updateFormData(update: (FormData) -> FormData) {
        val currentData = _formData.value ?: FormData()
        _formData.value = update(currentData)
    }

    fun solicitarAccesoIncidente(id: Int, onSuccess: () -> Unit) {
        val formData = _formData.value
        val validationResult = validateFields(formData)
        if (!validationResult.isValid) {
            _validationErrors.value = validationResult.errors
            return
        }

            val solicitudRequest = SolicitudRequest(
                nombreCompleto = formData.name,
                identificador = formData.idt,
                domicilio = formData.address,
                distrito = formData.city,
                correoElectronico = formData.email,
                telefono = formData.phone,
                motivo = formData.motive
            )

            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.value = true
                try {
                    val response = repository.solicitarIncidente(id, solicitudRequest)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                        _submissionMessage.value = "Solicitud enviada"
                    } else {
                        _submissionMessage.value = "Error en el envío de la solicitud"
                    }
                } catch (e: Exception) {
                    _submissionMessage.value = "Error en la red: ${e.localizedMessage}"
                } finally {
                    _isLoading.value = false
                }
            }
    }

    private fun validateFields(formData: FormData): FormValidationResult {
        val errors = mutableMapOf<String, String>()

        if (formData.name.isBlank()) errors["name"] = "El nombre está vacío."
        if (!formData.idt.matches(Regex("^(\\d{8}|[a-zA-Z0-9]{1,12}|\\d{11})$"))) {
            errors["idt"] = "Formato de identificador inválido."
        }
        if (formData.address.isBlank()) errors["address"] = "La dirección está vacía."
        if (formData.city.isBlank()) errors["city"] = "La ciudad está vacía."
        if (!Patterns.EMAIL_ADDRESS.matcher(formData.email).matches()) {
            errors["email"] = "Formato de correo inválido."
        }
        if (!formData.phone.matches(Regex("^(9\\d{8}|\\d{7,8})$"))) {
            errors["phone"] = "Formato de teléfono inválido."
        }
        if (formData.motive.isBlank()) errors["motive"] = "El motivo está vacío."

        // Actualiza el flujo de errores de validación
        _validationErrors.value = errors

        return FormValidationResult(errors.isEmpty(), errors)
    }

    fun resetNavigation() {
        _submissionMessage.value = null
    }

}