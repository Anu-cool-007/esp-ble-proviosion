package com.anuranjan.espprovision.presentation.custom_scanner_screen.use_case

import android.annotation.SuppressLint
import com.anuranjan.espprovision.common.Resource
import com.anuranjan.espprovision.model.ProvResult
import com.anuranjan.espprovision.service.ProvisionDeviceListener
import com.espressif.provisioning.ESPDevice
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProvisionDeviceUseCase @Inject constructor() {
    @SuppressLint("MissingPermission")
    operator fun invoke(
        ssid: String,
        passwd: String,
        espDevice: ESPDevice
    ): Flow<Resource<ProvResult>> = callbackFlow {
        trySend(Resource.Loading())



        val provisionDeviceListener = ProvisionDeviceListener(
            listenCreateSessionFailed = { e ->
                trySend(Resource.Error(appException = e))
                close(e)
                Unit
            },
            listenWifiConfigSent = {
                trySend(Resource.Success(ProvResult.WIFI_CONFIG_SENT))
            },
            listenWifiConfigFailed = { e ->
                trySend(Resource.Error(appException = e))
                close(e)
                Unit
            },
            listenWifiConfigApplied = {
                trySend(Resource.Success(ProvResult.WIFI_CONFIG_APPLIED))},
            listenWifiConfigApplyFailed = { e ->
                trySend(Resource.Error(appException = e))
                close(e)
                Unit
            },
            listenProvisioningFailedFromDevice = { cause ->
                trySend(Resource.Error(message = cause))
                close()
                Unit
            },
            listenDeviceProvisioningSuccess = {
                trySend(Resource.Success(ProvResult.DEVICE_PROVISIONED))
            },
            listenOnProvisioningFailed = { e ->
                trySend(Resource.Error(appException = e))
                close(e)
                Unit
            }
        )

        espDevice.provision(ssid, passwd, provisionDeviceListener)
        awaitClose { }
    }
}
