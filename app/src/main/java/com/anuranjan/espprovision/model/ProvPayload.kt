package com.anuranjan.espprovision.model

import com.google.gson.annotations.SerializedName

data class ProvPayload(
    @SerializedName("ver")
    val version: String,
    @SerializedName("name")
    val deviceName: String,
    @SerializedName("pop")
    val proofOfPossession: String = "",
    @SerializedName("transport")
    val transport: String,
    val security: Int? = 1,
    val password: String? = null
)
