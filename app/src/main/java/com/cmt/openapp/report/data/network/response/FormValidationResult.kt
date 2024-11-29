package com.cmt.openapp.report.data.network.response

data class FormValidationResult(
    val isValid: Boolean,
    val errors: Map<String, String>
)

