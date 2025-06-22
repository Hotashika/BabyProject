package com.example.bebegim.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bebegim.data.GetThermalData
import com.example.bebegim.model.VitalData
import com.example.bebegim.model.VitalType
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.example.bebegim.ui.components.HelpButton
import com.example.bebegim.R
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.ui.theme.Poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToChatbot: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val isAdmin = remember { mutableStateOf(true) }
    var meanTemp by remember { mutableStateOf<Double?>(null) }
    val getThermalData = remember { GetThermalData() }

    LaunchedEffect(Unit) {
        while (true) {
            val temp = getThermalData.fetchMeanTemperature()
            meanTemp = temp
            delay(2000L)
        }
    }

    val vitals = remember(meanTemp) {
        listOf(
            VitalData(VitalType.BABY_TEMPERATURE, meanTemp?.let { "%.1f°C".format(it) } ?: "Yükleniyor...", "Normal", true),
            VitalData(VitalType.SLEEP, "12 saat", "Yeterli", true),
            VitalData(VitalType.HUMIDITY, "45%", "Normal", true),
            VitalData(VitalType.ROOM_TEMPERATURE, "22.0°C", "Optimal", true),
            VitalData(VitalType.CO2, "400 ppm", "İyi", true)
        )
    }

    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
    Scaffold(
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hoş Geldiniz!",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Notification click action
                        }
                    ) {
                        Box {
                            Icon(
                                painter = painterResource(R.drawable.bell_24),
                                contentDescription = "Bildirimler",
                                tint = colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "home",
                onHomeClick = { },
                onChatClick = onNavigateToChatbot,
                onReportsClick = onNavigateToReports,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Live Video
            Text(
                text = "Video",
                style = MaterialTheme.typography.titleLarge
            // Separator line above video
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.2.dp)
                    .background(colorScheme.outline.copy(alpha = 0.3f))
            )

            // Video box with black background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Video",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            // Extra space between video and vitals
            Spacer(modifier = Modifier.height(0.2.dp))

            // Baby info header
            Text(
                text = "Bebek Bilgileri",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )

            // Important vitals
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VitalCard(vital = vitals[0], modifier = Modifier.weight(1f)) // BABY_TEMPERATURE
                VitalCard(vital = vitals[1], modifier = Modifier.weight(1f)) // SLEEP
            }

            // Environment info header
            Text(
                text = "Ortam Bilgileri",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )

            // Environment vitals
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VitalCard(vital = vitals[2], modifier = Modifier.weight(1f)) // HUMIDITY
                    VitalCard(vital = vitals[3], modifier = Modifier.weight(1f)) // ROOM_TEMPERATURE
                }
                VitalCard(vital = vitals[4], modifier = Modifier.fillMaxWidth()) // CO2
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun VitalCard(vital: VitalData, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        ),
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = vital.type.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = vital.value,
                style = MaterialTheme.typography.headlineSmall,
                color = colorScheme.primary,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = vital.status,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.secondary,
                fontFamily = Poppins,
                fontWeight = FontWeight.Light
            )
        }
    }
}
}
