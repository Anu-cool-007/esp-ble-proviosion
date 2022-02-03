package com.anuranjan.espprovision.presentation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object CustomScannerScreen : Screen("custom_scanner_screen")
    object EspScannerScreen : Screen("esp_scanner_screen")
}
