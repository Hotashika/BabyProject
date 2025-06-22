package com.example.bebegim.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bebegim.model.VitalData
import com.example.bebegim.model.VitalType

@Composable
fun HelpButton(
    onEmailSupport: () -> Unit,
    onLiveSupport: () -> Unit,
    onDocuments: () -> Unit,
    onSystemStatus: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Info, contentDescription = "Yardım & Destek")
        Text(text = "Yardım & Destek")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Yardım & Destek") },
            text = {
                Column {
                    Button(onClick = { onEmailSupport(); showDialog = false }) {
                        Text("E-posta Desteği")
                    }
                    Button(onClick = { onLiveSupport(); showDialog = false }) {
                        Text("Canlı Destek")
                    }
                    Button(onClick = { onDocuments(); showDialog = false }) {
                        Text("Dökümanlar")
                    }
                    Button(onClick = { onSystemStatus(); showDialog = false }) {
                        Text("Sistem Durumu")
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun VitalCard(
    vitalData: VitalData,
    modifier: Modifier = Modifier
) {
    val (icon, backgroundColor) = when (vitalData.type) {
        VitalType.BABY_TEMPERATURE -> Pair(
            Icons.Filled.ThumbUp,
            MaterialTheme.colorScheme.primaryContainer
        )
        VitalType.ROOM_TEMPERATURE -> Pair(
            Icons.Filled.Person,
            MaterialTheme.colorScheme.secondaryContainer
        )
        VitalType.HUMIDITY -> Pair(
            Icons.Filled.Edit,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = when (vitalData.type) {
                    VitalType.BABY_TEMPERATURE -> MaterialTheme.colorScheme.primary
                    VitalType.ROOM_TEMPERATURE -> MaterialTheme.colorScheme.secondary
                    VitalType.HUMIDITY -> MaterialTheme.colorScheme.tertiary
                },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = vitalData.type.displayName,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = vitalData.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (vitalData.isNormal)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            Text(
                text = vitalData.value,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
