package com.example.bebegim.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite

@Composable
fun ThermalScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToCalendarAndNOte: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
        bottomBar = {
            ThermalBottomBar(
                onNavigateToChatbot = onNavigateToChatbot,
                onNavigateToReports = onNavigateToReports,
                onNavigateToCalendarAndNOte = onNavigateToCalendarAndNOte,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 9:16 oranında görüntü frame'i
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Buraya görüntü içeriği eklenebilir
                // Örnek: Image, Camera preview, vs.
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Diğer UI elementleri buraya eklenebilir
        }
    }
}

@Composable
fun ThermalBottomBar(
    onNavigateToChatbot: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToCalendarAndNOte: () -> Unit,
    onNavigateBack: () -> Unit
) {
    com.example.bebegim.ui.components.BottomNavBar(
        currentRoute = "thermal_camera",
        onChatClick = onNavigateToChatbot,
        onReportsClick = onNavigateToReports,
        onCalendarAndNotesClick = onNavigateToCalendarAndNOte,
        onHomeClick = onNavigateBack,
        onThermalCameraClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun ThermalScreenPreview() {
    ThermalScreen(
        onNavigateBack = { },
        onNavigateToChatbot = { },
        onNavigateToReports = { },
        onNavigateToCalendarAndNOte = { }
    )
}