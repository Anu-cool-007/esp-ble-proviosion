package com.anuranjan.espprovision.presentation.custom_scanner_screen.state

data class ProvState(
    val isProvFailed: Boolean = false,
    val errMessage: String= "",
    val isWifiConfigSent: Boolean = false,
    val isWifiConfigApplied: Boolean = false,
    val isDeviceProvisioned: Boolean = false,
)
