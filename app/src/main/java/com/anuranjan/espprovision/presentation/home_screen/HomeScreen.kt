package com.anuranjan.espprovision.presentation.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anuranjan.espprovision.presentation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                shape = RoundedCornerShape(35),
                onClick = { navigateTo(navController, Screen.CustomScannerScreen.route) }) {
                Text(text = "Custom Scanner")
            }
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedButton(
                shape = RoundedCornerShape(35),
                onClick = { navigateTo(navController, Screen.EspScannerScreen.route) }) {
                Text(text = "ESP Scanner")
            }
        }
    }
}

private fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route)
}
