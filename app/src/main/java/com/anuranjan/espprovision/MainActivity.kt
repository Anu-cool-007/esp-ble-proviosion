package com.anuranjan.espprovision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.anuranjan.espprovision.presentation.NavConfig
import com.anuranjan.espprovision.presentation.Screen
import com.anuranjan.espprovision.ui.theme.ESPProvisionTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ESPProvisionTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavConfig(navController = navController, firstScreen = Screen.HomeScreen)
                }
            }
        }
    }
}
