package com.anuranjan.espprovision.presentation.custom_scanner_screen.use_case

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.anuranjan.espprovision.common.Resource
import com.anuranjan.espprovision.service.BLEScanListener
import com.espressif.provisioning.ESPProvisionManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BleScanUseCase @Inject constructor() {
    @SuppressLint("MissingPermission")
    operator fun invoke(espProvisionManager: ESPProvisionManager): Flow<Resource<MutableList<BluetoothDevice?>>> =
        callbackFlow {
            trySend(Resource.Loading())

            val scannedDevices: MutableList<BluetoothDevice?> =
                emptyList<BluetoothDevice?>().toMutableList()

            val bleScanListener = BLEScanListener(
                listenFailure = { e ->
                    trySend(Resource.Error(appException = e))
                    close(e)
                    Unit
                },
                listenScanCompleted = {
                    trySend(Resource.Success(scannedDevices))
                    Unit
                },
                listenScanStartFailed = {
                    trySend(Resource.Error(message = "BLE scan failed to start"))
                    close()
                    Unit
                },
                listenPeripheralFound = { bluetoothDevice, _ ->
                    scannedDevices.add(bluetoothDevice)
                }
            )
            espProvisionManager.searchBleEspDevices(bleScanListener)
            awaitClose { espProvisionManager.stopBleScan() }
        }
}