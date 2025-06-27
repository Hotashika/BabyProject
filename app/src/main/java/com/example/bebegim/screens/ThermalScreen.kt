package com.example.bebegim.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bebegim.data.GetThermalData
import com.example.bebegim.data.ThermalData
import com.example.bebegim.model.VitalData
import com.example.bebegim.model.VitalType
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.ui.theme.Poppins
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThermalScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToCalendarAndNOte: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()

    var thermalData by remember { mutableStateOf<ThermalData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }

    val thermalDataProvider = remember { GetThermalData() }
    var headTemp by remember { mutableStateOf<Double?>(null) }
    var upperBodyTemp by remember { mutableStateOf<Double?>(null) }
    var lowerBodyTemp by remember { mutableStateOf<Double?>(null) }
    val getThermalData = remember { GetThermalData() }

    /*LaunchedEffect(Unit) {
        while (true) {
            val temp = getThermalData.fetchHeadUpLowBodyTemperature()
            if (temp != null) {
                headTemp = temp.first
                upperBodyTemp = temp.second
                lowerBodyTemp = temp.third
            } else {
                headTemp = null
                upperBodyTemp = null
                lowerBodyTemp = null
            }
            delay(20000)
        }
    }*/

    val vitals = remember(headTemp, upperBodyTemp, lowerBodyTemp) {
        listOf(
            VitalData(
                VitalType.BABY_HEAD_TEMPERATURE,
                headTemp?.let { "%.1f°C".format(it) } ?: "Yükleniyor...",
                getTemperatureStatus(headTemp),
                true
            ),
            VitalData(
                VitalType.BABY_UPPERBODY_TEMPERATURE,
                upperBodyTemp?.let { "%.1f°C".format(it) } ?: "Yükleniyor...",
                getTemperatureStatus(upperBodyTemp),
                true
            ),
            VitalData(
                VitalType.BABY_LOWERBODY_TEMPERATURE,
                lowerBodyTemp?.let { "%.1f°C".format(it) } ?: "Yükleniyor...",
                getTemperatureStatus(lowerBodyTemp),
                true
            )
        )
    }

    // Veri çekme ve otomatik yenileme
    LaunchedEffect(Unit) {
        while (true) {
            try {
                isLoading = true
                errorMessage = null

                val data = thermalDataProvider.fetchThermalData()
                val url = thermalDataProvider.fetchThermalImageUrl()

                if (data != null) {
                    thermalData = data
                    imageUrl = url
                } else {
                    errorMessage = "Veri alınamadı"
                }
            } catch (e: Exception) {
                errorMessage = "Hata: ${e.message}"
            } finally {
                isLoading = false
            }

            // 20 saniyede bir güncelle
            delay(10000)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Termal Kamera",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Thermal görüntü frame'i
            Box(
                modifier = Modifier
                    .width(300.dp)
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
                when {
                    isLoading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Görüntü yükleniyor...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    errorMessage != null -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 32.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    imageUrl != null -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .memoryCachePolicy(coil.request.CachePolicy.DISABLED)
                                .diskCachePolicy(coil.request.CachePolicy.DISABLED)
                                .build(),
                            contentDescription = "Termal Görüntü",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onError = {
                                errorMessage = "Görüntü yüklenemedi"
                            }
                        )
                    }

                    else -> {
                        Text(
                            text = "Görüntü bulunamadı",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ThermalVitals(vitals[0], modifier = Modifier.weight(1f))
                ThermalVitals(vitals[1], modifier = Modifier.weight(1f))
                ThermalVitals(vitals[2], modifier = Modifier.weight(1f))
            }


        }
    }
}

@Composable
fun ThermalVitals(vital : VitalData, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme

    Card (
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        ),
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = vital.type.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp
            )
            Text(
                text = vital.value,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.primary,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = vital.status,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.secondary,
                fontFamily = Poppins,
                fontWeight = FontWeight.Light,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
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