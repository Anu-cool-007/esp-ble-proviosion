package com.anuranjan.espprovision.presentation.custom_scanner_screen

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anuranjan.espprovision.common.Resource
import com.anuranjan.espprovision.model.BleScanResult
import com.anuranjan.espprovision.model.ProvPayload
import com.anuranjan.espprovision.model.ProvResult
import com.anuranjan.espprovision.presentation.custom_scanner_screen.state.BleScanState
import com.anuranjan.espprovision.presentation.custom_scanner_screen.state.ProvState
import com.anuranjan.espprovision.presentation.custom_scanner_screen.state.ScannerState
import com.anuranjan.espprovision.presentation.custom_scanner_screen.use_case.BleScanUseCase
import com.anuranjan.espprovision.presentation.custom_scanner_screen.use_case.ProvisionDeviceUseCase
import com.espressif.provisioning.DeviceConnectionEvent
import com.espressif.provisioning.ESPConstants
import com.espressif.provisioning.ESPProvisionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@HiltViewModel
class CustomScannerViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val bleScanUseCase: BleScanUseCase,
    private val provisionDeviceUseCase: ProvisionDeviceUseCase
) : ViewModel() {
    private val espProvisionManager = ESPProvisionManager.getInstance(context)

    private val _cameraState = mutableStateOf(ScannerState())
    val cameraState: State<ScannerState> = _cameraState

    private val _bleScanState = mutableStateOf(BleScanState())
    val bleScanState: State<BleScanState> = _bleScanState

    private val _provState = mutableStateOf(ProvState())
    val provState: State<ProvState> = _provState

    init {
        espProvisionManager.createESPDevice(
            ESPConstants.TransportType.TRANSPORT_BLE,
            ESPConstants.SecurityType.SECURITY_1
        )
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun bleDeviceConnectEventBus(event: DeviceConnectionEvent) {
        provisionDevice()
    }

    fun setProvPayload(provPayload: ProvPayload) {
        _cameraState.value = ScannerState(isQRCodeScanned = true, provPayload = provPayload)
        espProvisionManager.espDevice.deviceName = provPayload.deviceName
        espProvisionManager.espDevice.proofOfPossession = provPayload.proofOfPossession
        startBleScan(provPayload)
    }

    private fun startBleScan(provPayload: ProvPayload) {
        bleScanUseCase(
            devicePrefix = provPayload.deviceName,
            espProvisionManager = espProvisionManager
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _bleScanState.value = _bleScanState.value.copy(isBleScanning = true)
                }
                is Resource.Error -> {
                    _bleScanState.value = BleScanState(
                        isBleScanFailed = true,
                        errMessage = result.message ?: "BLE scan failed"
                    )
                }
                is Resource.Success -> {
                    when(result.data){
                        BleScanResult.DEVICE_SCANNED -> {
                            _bleScanState.value = _bleScanState.value.copy(
                                isDeviceFound = true
                            )
                        }
                        BleScanResult.BLE_SCAN_FINISHED -> {
                            _bleScanState.value = _bleScanState.value.copy(
                                isScanComplete = true
                            )
                        }
                    }

                }
            }
        }.launchIn(viewModelScope)
    }

    private fun provisionDevice() {

        provisionDeviceUseCase(
            ssid = "Yaman 2.4G",
            passwd = "bsnl2000",
            espDevice = espProvisionManager.espDevice
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _provState.value = ProvState(isProvStarted = true)
                }
                is Resource.Error -> {
                    _provState.value = ProvState(
                        isProvFailed = true,
                        errMessage = result.message ?: "Provisioned Failed"
                    )
                }
                is Resource.Success -> {
                    when (result.data) {
                        ProvResult.WIFI_CONFIG_SENT -> {
                            _provState.value = _provState.value.copy(isWifiConfigSent = true)
                        }
                        ProvResult.WIFI_CONFIG_APPLIED -> {
                            _provState.value = _provState.value.copy(isWifiConfigApplied = true)
                        }
                        ProvResult.DEVICE_PROVISIONED -> {
                            _provState.value = _provState.value.copy(isDeviceProvisioned = true)
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}