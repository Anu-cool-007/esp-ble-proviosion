package com.anuranjan.espprovision.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anuranjan.espprovision.presentation.ui.theme.ESPProvisionTheme

@Composable
fun ProgressCard(isComplete: Boolean, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (!isComplete) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        } else {
            Canvas(modifier = Modifier.width(20.dp), onDraw = {
                val size = 20.dp.toPx()
                drawCircle(
                    color = Color.Green,
                    radius = size / 2f
                )
            })
        }
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = text)
    }
}

@Preview
@Composable
fun PreviewProgressCard() {
    ESPProvisionTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    ProgressCard(isComplete = false, text = "In Progress")
                    ProgressCard(isComplete = true, text = "In Progress")
                    ProgressCard(isComplete = false, text = "In Progress")
                    ProgressCard(isComplete = false, text = "In Progress")
                    ProgressCard(isComplete = false, text = "In Progress")
                    ProgressCard(isComplete = false, text = "In Progress")
                }
            }
        }
    }
}
