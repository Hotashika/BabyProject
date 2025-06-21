package com.example.bebegim.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bebegim.model.VitalData
import com.example.bebegim.model.VitalType
import com.example.bebegim.ui.theme.Poppins

@Composable
fun VitalCard(
    vitalData: VitalData,
    modifier: Modifier = Modifier
) {
    val (icon, backgroundColor) = when (vitalData.type) {
        VitalType.BABY_TEMPERATURE -> Pair(
            Icons.Default.ThumbUp,
            MaterialTheme.colorScheme.primaryContainer
        )

        VitalType.ROOM_TEMPERATURE -> Pair(
            Icons.Default.Person,
            MaterialTheme.colorScheme.secondaryContainer
        )

        VitalType.HUMIDITY -> Pair(
            Icons.Default.Edit,
            MaterialTheme.colorScheme.tertiaryContainer
        )
        VitalType.SLEEP -> Pair(
            Icons.Default.Star,
            MaterialTheme.colorScheme.primaryContainer
        )
        VitalType.CO2 -> Pair(
            Icons.Default.Favorite,
            MaterialTheme.colorScheme.secondaryContainer
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
                    VitalType.SLEEP -> MaterialTheme.colorScheme.primary
                    VitalType.CO2 -> MaterialTheme.colorScheme.secondary
                },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = vitalData.type.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Text(
                    text = vitalData.status,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal
                    ),
                    color = if (vitalData.isNormal)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            Text(
                text = vitalData.value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}