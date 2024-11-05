package com.cmt.openapp.report.data.network.response

import com.google.gson.annotations.SerializedName

data class SolicitudRequest(
    @SerializedName("nombreCompleto") val nombreCompleto: String,
    @SerializedName("identificador") val identificador: String,
    @SerializedName("domicilio") val domicilio: String,
    @SerializedName("distrito") val distrito: String,
    @SerializedName("correoElectronico") val correoElectronico: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("motivo") val motivo: String
)