package com.anuranjan.espprovision.presentation.custom_scanner_screen.state

import android.bluetooth.BluetoothDevice

data class BleScanState(
    val isBleScanning: Boolean = false,
    val isBleScanFailed: Boolean = false,
    val errMessage: String = "",
    val isScanComplete: Boolean = false,
    val scannedDevices: MutableList<BluetoothDevice?> =
        emptyList<BluetoothDevice?>().toMutableList()
)
