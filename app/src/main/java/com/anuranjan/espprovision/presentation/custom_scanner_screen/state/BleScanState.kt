package com.anuranjan.espprovision.presentation.custom_scanner_screen.state

data class BleScanState(
    val isBleScanning: Boolean = false,
    val isBleScanFailed: Boolean = false,
    val errMessage: String = "",
    val isDeviceFound: Boolean = false,
    val isScanComplete: Boolean = false
)
