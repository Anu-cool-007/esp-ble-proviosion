package com.anuranjan.espprovision.service

import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.anuranjan.espprovision.model.ProvPayload
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QRAnalyzer(private val onQrCodeScanned: (ProvPayload) -> Unit) : ImageAnalysis.Analyzer {

    private val supportedImageFormatList = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888
    )

    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormatList) {
            val rawData = image.planes.first().buffer.toByteArray()

            val source = PlanarYUVLuminanceSource(
                rawData,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            try {
                val result = MultiFormatReader().apply {
                    setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.QR_CODE)))
                }.decode(binaryBitmap)

                onQrCodeScanned(Gson().fromJson(result.text, ProvPayload::class.java))
            } catch (e: JsonSyntaxException) {
                // GSON can't find json text
                Log.e("QR ANALYZER", e.message?: "JSON ERROR")

            } catch (e: Exception) {
            } finally {
                image.close()
            }
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also { get(it) }
    }
}