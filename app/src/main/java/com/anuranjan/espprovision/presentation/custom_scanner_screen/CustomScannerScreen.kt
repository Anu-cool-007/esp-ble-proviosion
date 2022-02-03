package com.anuranjan.espprovision.presentation.custom_scanner_screen

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.anuranjan.espprovision.presentation.common.RequestPermsWrapper
import com.anuranjan.espprovision.service.QRAnalyzer
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

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    var qrCode by remember { mutableStateOf("") }
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        RequestPermsWrapper(permissionList = permissionList) {
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)

                    val preview = Preview.Builder().build()

                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        QRAnalyzer { result ->
                            qrCode = result
                        }
                    )
                    try {
                        cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    previewView
                },
                modifier = Modifier.weight(1f)
            )
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