package com.anuranjan.espprovision.service

import com.anuranjan.espprovision.common.AppException
import com.anuranjan.espprovision.common.toAppException
import com.espressif.provisioning.ESPConstants
import com.espressif.provisioning.listeners.ProvisionListener

class ProvisionDeviceListener(
    val listenCreateSessionFailed: (AppException) -> Unit,
    val listenWifiConfigSent: () -> Unit,
    val listenWifiConfigFailed: (AppException) -> Unit,
    val listenWifiConfigApplied: () -> Unit,
    val listenWifiConfigApplyFailed: (AppException) -> Unit,
    val listenProvisioningFailedFromDevice: (String) -> Unit,
    val listenDeviceProvisioningSuccess: () -> Unit,
    val listenOnProvisioningFailed: (AppException) -> Unit,
) : ProvisionListener {
    override fun createSessionFailed(e: Exception?) {
        listenCreateSessionFailed(e?.toAppException() ?: defaultException)
    }

    override fun wifiConfigSent() = listenWifiConfigSent()

    override fun wifiConfigFailed(e: Exception?) {
        listenWifiConfigFailed(e?.toAppException() ?: defaultException)
    }

    override fun wifiConfigApplied() = listenWifiConfigApplied()

    override fun wifiConfigApplyFailed(e: Exception?) {
        listenWifiConfigApplyFailed(e?.toAppException() ?: defaultException)
    }

    override fun provisioningFailedFromDevice(failureReason: ESPConstants.ProvisionFailureReason?) {
        val message = when (failureReason) {
            ESPConstants.ProvisionFailureReason.AUTH_FAILED -> "Authentication Error"
            ESPConstants.ProvisionFailureReason.NETWORK_NOT_FOUND -> "Network not Found"
            ESPConstants.ProvisionFailureReason.DEVICE_DISCONNECTED -> "Device Disconnected"
            ESPConstants.ProvisionFailureReason.UNKNOWN -> "Unknown reason"
            null -> "Unknown reason"
        }
        listenProvisioningFailedFromDevice(message)
    }

    override fun deviceProvisioningSuccess() = listenDeviceProvisioningSuccess()

    override fun onProvisioningFailed(e: Exception?) {
        listenOnProvisioningFailed(e?.toAppException() ?: defaultException)
    }

    companion object {
        val defaultException = AppException(message = "Unknown error occurred")
    }
}
