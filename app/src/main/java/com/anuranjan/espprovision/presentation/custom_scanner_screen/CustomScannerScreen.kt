package com.anuranjan.espprovision.presentation.custom_scanner_screen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anuranjan.espprovision.model.ProvPayload
import com.anuranjan.espprovision.presentation.common.ProgressCard
import com.anuranjan.espprovision.presentation.common.RequestPermsWrapper
import com.espressif.provisioning.ESPConstants
import com.espressif.provisioning.ESPProvisionManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.gson.Gson

@ExperimentalPermissionsApi
@Composable
fun CustomScannerScreen(viewModel: CustomScannerViewModel = hiltViewModel()) {
    val state = viewModel.state.value

    val permissionList = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val context = LocalContext.current
    val espProvManager = ESPProvisionManager.getInstance(context)

    val espDevice = espProvManager.createESPDevice(
        ESPConstants.TransportType.TRANSPORT_BLE,
        ESPConstants.SecurityType.SECURITY_1
    )

//    val bleScanListener = BLEScanListener()
//
//    if (ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    ) {
//        espProvManager.searchBleEspDevices("", bleScanListener)
//    }

    RequestPermsWrapper(permissionList = permissionList) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (!state.isQRCodeScanned) {
                QRScanner(modifier = Modifier.fillMaxSize()) { result ->
                    viewModel.setProvPayload(result)
                }
            } else {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    ProgressCard(isComplete = state.isQRCodeScanned, text = "Scanned QR code")
                    ProgressCard(isComplete = state.isDeviceFound, text = "Device Found")
                    ProgressCard(
                        isComplete = state.isDeviceProvisioned,
                        text = "Device Provisioned"
                    )
                    ProgressCard(
                        isComplete = state.isDeviceConnectedToWifi,
                        text = "Device Connected to Wifi"
                    )
                }
            }
        }
    }
}
