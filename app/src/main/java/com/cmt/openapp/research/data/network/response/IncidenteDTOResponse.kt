package com.cmt.openapp.research.data.network.response

import com.google.gson.annotations.SerializedName

data class IncidenteDTOResponse(
    @SerializedName("nroIncidente") val nroIncidente: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("hora") val hora: String,
    @SerializedName("tipoIncidente") val tipoIncidente: String,
)