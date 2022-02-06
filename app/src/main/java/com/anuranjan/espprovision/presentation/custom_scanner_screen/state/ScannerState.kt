package com.anuranjan.espprovision.presentation.custom_scanner_screen.state

import com.anuranjan.espprovision.model.ProvPayload

data class ScannerState(
    val isQRCodeScanned: Boolean = false,
    val provPayload: ProvPayload? = null,
)
