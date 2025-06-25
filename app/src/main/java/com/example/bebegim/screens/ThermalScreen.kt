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
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import kotlinx.coroutines.delay

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

            // 2 saniyede bir güncelle
            delay(20000)
        }
    }

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
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Başlık
            Text(
                text = "Termal Kamera",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Thermal görüntü frame'i
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

            // Sıcaklık bilgileri
            thermalData?.let { data ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Sıcaklık Bilgileri",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Maksimum",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${String.format("%.1f", data.maxTemperature)}°C",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            }

                            Column {
                                Text(
                                    text = "Minimum",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${String.format("%.1f", data.minTemperature)}°C",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Ortalama",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${String.format("%.1f", data.meanTemperature)}°C",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Column {
                                Text(
                                    text = "Merkez",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${String.format("%.1f", data.centerTemperature)}°C",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
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