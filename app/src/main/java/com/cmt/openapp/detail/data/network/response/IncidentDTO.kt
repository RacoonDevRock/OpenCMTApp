package com.cmt.openapp.detail.data.network.response

import com.google.gson.annotations.SerializedName

data class IncidentDTO(
    @SerializedName("nroIncidente") val nroIncidente: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("horallamada") val horallamada: String,
    @SerializedName("tipoIncidente") val tipoIncidente: String,
    @SerializedName("zona") val zona: String,
    @SerializedName("sector") val sector: String,
    @SerializedName("tipoIntervencion") val tipoIntervencion: String,
    @SerializedName("resultado") val resultado: String,
)