package com.anuranjan.espprovision.presentation.custom_scanner_screen.use_case

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.text.TextUtils
import com.anuranjan.espprovision.common.Resource
import com.anuranjan.espprovision.model.BleScanResult
import com.anuranjan.espprovision.service.BLEScanListener
import com.espressif.provisioning.ESPProvisionManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BleScanUseCase @Inject constructor() {
    @SuppressLint("MissingPermission")
    operator fun invoke(
        devicePrefix: String = "",
        espProvisionManager: ESPProvisionManager
    ): Flow<Resource<BleScanResult>> = callbackFlow {
        trySend(Resource.Loading())

        var isDeviceFound = false

        val bleScanListener = BLEScanListener(
            listenFailure = { e ->
                trySend(Resource.Error(appException = e))
                close(e)
                Unit
            },
            listenScanCompleted = {
                    trySend(Resource.Success(data = BleScanResult.BLE_SCAN_FINISHED))
                Unit
            },
            listenScanStartFailed = {
                trySend(Resource.Error(message = "BLE scan failed to start"))
                close()
                Unit
            },
            listenPeripheralFound = { bluetoothDevice, scanResult ->
                scanResult?.scanRecord?.let { scanRecord ->
                    if (!isDeviceFound && bluetoothDevice != null && !TextUtils.isEmpty(scanRecord.deviceName)) {
                        if (scanRecord.deviceName == espProvisionManager.espDevice.deviceName) {
                            // Device found
                            isDeviceFound = true
                            var serviceUuid = ""
                            if (scanRecord.serviceUuids != null && scanRecord.serviceUuids.size > 0) {
                                serviceUuid = scanRecord.serviceUuids[0].toString()
                            }
                            espProvisionManager.espDevice.bluetoothDevice = bluetoothDevice
                            espProvisionManager.espDevice.primaryServiceUuid = serviceUuid
                            espProvisionManager.espDevice.connectToDevice()

                            espProvisionManager.stopBleScan()
                            trySend(Resource.Success(data = BleScanResult.DEVICE_SCANNED))
                        }
                    }
                }
            }
        )
        espProvisionManager.searchBleEspDevices(devicePrefix, bleScanListener)
        awaitClose { espProvisionManager.stopBleScan() }
    }
}