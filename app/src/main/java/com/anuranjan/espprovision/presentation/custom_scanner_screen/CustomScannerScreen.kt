package com.anuranjan.espprovision.presentation.custom_scanner_screen

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anuranjan.espprovision.presentation.common.RequestPermsWrapper
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun CustomScannerScreen() {
    val permissionList = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var qrCode by remember { mutableStateOf("") }

    RequestPermsWrapper(permissionList = permissionList) {
        Column(modifier = Modifier.fillMaxSize()) {
            QRScanner(modifier = Modifier.weight(1f)) { result ->
                qrCode = result
            }
            Text(
                text = qrCode,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
    }
}