package com.anuranjan.espprovision.presentation.common

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun RequestPermsWrapper(
    permissionList: List<String>,
    content: @Composable () -> Unit
) {
    val cameraPermissionState = rememberMultiplePermissionsState(permissionList)
    val context = LocalContext.current

    PermissionsRequired(
        multiplePermissionsState = cameraPermissionState,
        permissionsNotGrantedContent = {
            PermDialog(
                text = "The camera is important for this app. Please grant the permission.",
                btnTxt = "Ok!"
            ) {
                cameraPermissionState.launchMultiplePermissionRequest()
            }
        },
        permissionsNotAvailableContent = {
            PermDialog(
                text = "Camera permission denied. Please, grant us access on the Settings screen.",
                btnTxt = "Open Settings"
            ) {
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                )
            }
        }
    ) {
        content()
    }
}

@Composable
fun PermDialog(
    text: String,
    btnTxt: String,
    btnOnClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                shape = RoundedCornerShape(35),
                onClick = { btnOnClick() }) {
                Text(text = btnTxt)
            }
        }
    }
}

@Composable
@Preview
fun PreviewPermDialog() {
    Surface {
        PermDialog(text = "Help Me", btnTxt = "NO!") { }
    }
}
