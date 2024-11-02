package com.cmt.openapp.detail.data.network.response

import com.google.gson.annotations.SerializedName

data class IncidentDTODetail(
    @SerializedName("nroIncidente") val nroIncidente: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("horallamada") val horallamada: String,
    @SerializedName("tipoIncidente") val tipoIncidente: String,
    @SerializedName("zona") val zona: String,
    @SerializedName("sector") val sector: String,
    @SerializedName("tipoIntervencion") val tipoIntervencion: String,
    @SerializedName("resultado") val resultado: String,
    @SerializedName("_links") val links: Links
)

data class Links(
    @SerializedName("self") val self: Link,
    @SerializedName("todos") val todos: Link
)

data class Link(
    @SerializedName("href") val href: String
)