package com.anuranjan.espprovision.presentation.custom_scanner_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.anuranjan.espprovision.model.ProvPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomScannerViewModel @Inject constructor (): ViewModel() {
    private val _state = mutableStateOf(CustomScannerState())

    val state: State<CustomScannerState> = _state

    fun setProvPayload(provPayload: ProvPayload) {
        _state.value = CustomScannerState(isQRCodeScanned = true, provPayload = provPayload)
    }
}