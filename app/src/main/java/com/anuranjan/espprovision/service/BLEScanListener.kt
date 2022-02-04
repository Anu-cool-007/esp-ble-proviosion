package com.anuranjan.espprovision.service

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import com.espressif.provisioning.listeners.BleScanListener
import java.lang.Exception

class BLEScanListener(
    val listenScanStartFailed: () -> Unit,
    val listenPeripheralFound: (BluetoothDevice?, ScanResult?) -> Unit,
    val listenScanCompleted: () -> Unit,
    val listenFailure: (Exception?) -> Unit
): BleScanListener {
    override fun scanStartFailed() {
        listenScanStartFailed()
    }

    override fun onPeripheralFound(device: BluetoothDevice?, scanResult: ScanResult?) {
        listenPeripheralFound(device, scanResult)
    }

    override fun scanCompleted() {
        listenScanCompleted()
    }

    override fun onFailure(e: Exception?) {
        listenFailure(e)
    }
}
