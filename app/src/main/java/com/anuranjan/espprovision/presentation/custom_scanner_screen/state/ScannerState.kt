package com.anuranjan.espprovision.presentation.custom_scanner_screen.state

import com.anuranjan.espprovision.model.ProvPayload

data class ScannerState(
    var isQRCodeScanned: Boolean = false,
    var provPayload: ProvPayload? = null,
)
