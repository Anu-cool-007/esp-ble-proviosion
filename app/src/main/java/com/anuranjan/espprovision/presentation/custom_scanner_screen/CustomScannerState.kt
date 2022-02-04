package com.anuranjan.espprovision.presentation.custom_scanner_screen

import com.anuranjan.espprovision.model.ProvPayload

data class CustomScannerState(
    var isQRCodeScanned: Boolean = false,
    var isDeviceFound: Boolean = false,
    var isDeviceProvisioned: Boolean = false,
    var isDeviceConnectedToWifi: Boolean = false,
    var provPayload: ProvPayload? = null,
)
