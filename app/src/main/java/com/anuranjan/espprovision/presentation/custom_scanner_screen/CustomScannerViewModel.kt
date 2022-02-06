package com.anuranjan.espprovision.presentation.custom_scanner_screen

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anuranjan.espprovision.common.Resource
import com.anuranjan.espprovision.model.ProvPayload
import com.anuranjan.espprovision.presentation.custom_scanner_screen.state.BleScanState
import com.anuranjan.espprovision.presentation.custom_scanner_screen.state.ProvState
import com.anuranjan.espprovision.presentation.custom_scanner_screen.state.ScannerState
import com.anuranjan.espprovision.presentation.custom_scanner_screen.use_case.BleScanUseCase
import com.espressif.provisioning.ESPProvisionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CustomScannerViewModel @Inject constructor(
    private val bleScanUseCase: BleScanUseCase,
    @ApplicationContext context: Context,
) : ViewModel() {
    private val espProvisionManager = ESPProvisionManager.getInstance(context)

    private val _cameraState = mutableStateOf(ScannerState())
    val cameraState: State<ScannerState> = _cameraState

    private val _bleScanState = mutableStateOf(BleScanState())
    val bleScanState: State<BleScanState> = _bleScanState

    private val _provState = mutableStateOf(ProvState())
    val provState: State<ProvState> = _provState

    fun setProvPayload(provPayload: ProvPayload) {
        _cameraState.value = ScannerState(isQRCodeScanned = true, provPayload = provPayload)
        startBleScan()
    }

    private fun startBleScan() {
        bleScanUseCase(espProvisionManager).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _bleScanState.value = BleScanState(
                        isBleScanFailed = true,
                        errMessage = result.message ?: "BLE scan failed"
                    )
                }
                is Resource.Loading -> {
                    _bleScanState.value = BleScanState(isBleScanning = true)
                }
                is Resource.Success -> {
                    _bleScanState.value = BleScanState(
                        isScanComplete = true,
                        scannedDevices = result.data
                            ?: emptyList<BluetoothDevice?>().toMutableList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}