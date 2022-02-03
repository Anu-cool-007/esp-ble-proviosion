package com.anuranjan.espprovision.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anuranjan.espprovision.presentation.custom_scanner_screen.CustomScannerScreen
import com.anuranjan.espprovision.presentation.esp_scanner_screen.EspScannerScreen
import com.anuranjan.espprovision.presentation.home_screen.HomeScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalPermissionsApi
@Composable
fun NavConfig(
    navController: NavHostController,
    firstScreen: Screen
) {
    NavHost(navController = navController, startDestination = firstScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.CustomScannerScreen.route) {
            CustomScannerScreen()
        }
        composable(route = Screen.EspScannerScreen.route) {
            EspScannerScreen()
        }
    }
}