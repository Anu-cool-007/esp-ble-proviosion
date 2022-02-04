package com.anuranjan.espprovision.presentation.custom_scanner_screen

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.anuranjan.espprovision.model.ProvPayload
import com.anuranjan.espprovision.service.QRAnalyzer

@Composable
fun QRScanner(modifier: Modifier = Modifier, onCodeScanned: (ProvPayload) -> Unit) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentContext = LocalContext.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(currentContext)
    }
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
                    onCodeScanned(result)
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
        modifier = modifier
    )
}