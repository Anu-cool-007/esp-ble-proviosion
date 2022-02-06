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
import com.anuranjan.espprovision.presentation.common.ProgressCard
import com.anuranjan.espprovision.presentation.common.RequestPermsWrapper
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun CustomScannerScreen(viewModel: CustomScannerViewModel = hiltViewModel()) {
    val cameraState = viewModel.cameraState.value
    val bleScanState = viewModel.bleScanState.value
    val provState = viewModel.provState.value

    val permissionList = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val context = LocalContext.current

    RequestPermsWrapper(permissionList = permissionList) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (!cameraState.isQRCodeScanned) {
                QRScanner(modifier = Modifier.fillMaxSize()) { result ->
                    viewModel.setProvPayload(result)
                }
            } else {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    ProgressCard(isComplete = cameraState.isQRCodeScanned, text = "Scanned QR code")
                    ProgressCard(isComplete = bleScanState.isScanComplete, text = "Device Found")
                    ProgressCard(
                        isComplete = provState.isWifiConfigSent,
                        text = "Wifi Config Sent"
                    )
                    ProgressCard(
                        isComplete = provState.isWifiConfigApplied,
                        text = "Wifi Config Applied"
                    )
                    ProgressCard(
                        isComplete = provState.isDeviceProvisioned,
                        text = "Device Provisioned"
                    )
                }
            }
        }
    }
}
