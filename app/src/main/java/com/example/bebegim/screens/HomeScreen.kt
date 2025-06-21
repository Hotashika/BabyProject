package com.example.bebegim.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.example.bebegim.data.GetThermalData
import com.example.bebegim.model.VitalData
import com.example.bebegim.model.VitalType
import com.example.bebegim.ui.components.BottomNavBar
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

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

    // Temp Data
    LaunchedEffect(Unit) {
        while (true) {
            val temp = getThermalData.fetchMeanTemperature()
            meanTemp = temp
            delay(2000L)
        }
    }

    val vitals = remember(meanTemp) {
        listOf(
            VitalData(
                VitalType.BABY_TEMPERATURE,
                meanTemp?.let { "%.1f°C".format(it) } ?: "Yükleniyor...",
                "Normal",
                true
            ),
            VitalData(
                VitalType.ROOM_TEMPERATURE,
                "22.0°C",
                "Optimal",
                true
            ),
            VitalData(
                VitalType.HUMIDITY,
                "45%",
                "Normal",
                true
            ),
            VitalData(
                VitalType.HUMIDITY,
                "45%",
                "Normal",
                true
            ),
            VitalData(
                VitalType.HUMIDITY,
                "45%",
                "Normal",
                true
            ),
            VitalData(
                VitalType.HUMIDITY,
                "45%",
                "Normal",
                true
            ),
            VitalData(
                VitalType.HUMIDITY,
                "45%",
                "Normal",
                true
            )
        )
    }

    Scaffold(

        // Unnecessary
        /*topBar = {
            TopAppBar(
                title = { Text("Hoş geldiniz !") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },*/
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Video
            Text(
                text = "Video",
                style = MaterialTheme.typography.titleLarge
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Video",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Live Data
            Text(
                text = "Veriler",
                style = MaterialTheme.typography.titleLarge
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp), // adjust as needed
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false // disables grid's own scroll, uses parent scroll
            ) {
                items(vitals) { vital ->
                    VitalCard(
                        vital = vital,
                        modifier = Modifier
                            .aspectRatio(1f) // makes the card square
                    )
                }
            }
        }
    }
}


@Composable
fun VitalCard(vital: VitalData, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = vital.type.displayName, style = MaterialTheme.typography.titleMedium)
            Text(text = vital.value, style = MaterialTheme.typography.headlineSmall)
            Text(text = vital.status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

