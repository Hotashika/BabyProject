package com.example.bebegim.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bebegim.data.GetThermalData
import com.example.bebegim.data.GetVideo
import com.example.bebegim.model.VitalData
import com.example.bebegim.model.VitalType
import kotlinx.coroutines.delay
import com.example.bebegim.R
import com.example.bebegim.data.GetVideo
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.ui.theme.Poppins

fun getTemperatureStatus(temp: Double?): String {
    return when {
        temp == null -> "Yükleniyor..."
        temp < 36.5 -> "Düşük"
        temp in 36.5..37.5 -> "Normal"
        temp > 37.5 -> "Yüksek"
        else -> "Bilinmiyor"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToChatbot: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCalendarAndNotes: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val isAdmin = remember { mutableStateOf(true) }
    var meanTemp by remember { mutableStateOf<Double?>(null) }
    val getThermalData = remember { GetThermalData() }

    // Video için eklenen kısım
    val getVideo = remember { GetVideo() }
    val currentFrame by getVideo.getVideoStream().collectAsState(initial = null)


    // Bildirim pop-up için state
    var showNotificationDialog by remember { mutableStateOf(false) }

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
                getTemperatureStatus(meanTemp),
                true
            ),
            VitalData(VitalType.SLEEP, "12 saat", "Yeterli", true),
            VitalData(VitalType.HUMIDITY, "45%", "Yüksek", true),
            VitalData(VitalType.ROOM_TEMPERATURE, "22.0°C", "Optimal", true),
            VitalData(VitalType.CO2, "29 AQI", "İyi", true)
        )
    }

    var notifications by remember {
        mutableStateOf(listOf(
            "Bebek 2 saat önce uyandı",
            "Oda sıcaklığı optimal seviyede",
            "Son beslenme: 3 saat önce",
            "Nem oranı normale döndü",
            "Video kaydı başlatıldı"
        ))
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
                            showNotificationDialog = true
                        }
                    ) {
                        Box {
                            Icon(
                                painter = painterResource(R.drawable.bell_24),
                                contentDescription = "Bildirimler",
                                tint = colorScheme.onSurface
                            )
                            if (notifications.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            Color.Red,
                                            CircleShape
                                        )
                                        .align(Alignment.TopEnd)
                                        .offset(x = 2.dp, y = (-2).dp)
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = onNavigateToProfile
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.user_24),
                            contentDescription = "Profil",
                            tint = colorScheme.onSurface
                        )
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
                onProfileClick = onNavigateToProfile,
                onCalendarAndNotesClick = onNavigateToCalendarAndNotes,
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

            // Video kısmı - SADECE BU KISIM DEĞİŞTİRİLDİ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (currentFrame != null) {
                    // Video frame'i göster
                    Image(
                        bitmap = currentFrame!!.asImageBitmap(),
                        contentDescription = "Canlı Video",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Yüklenirken veya bağlantı yokken gösterilecek
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.bell_24), // Video ikonu kullanın
                            contentDescription = "Video Yükleniyor",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Video Yükleniyor...",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(0.2.dp))

            Text(
                text = "Bebek Bilgileri",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VitalCard(vital = vitals[0], modifier = Modifier.weight(1f))
                VitalCard(vital = vitals[1], modifier = Modifier.weight(1f))
            }

            Text(
                text = "Ortam Bilgileri",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )

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
                    VitalCard(vital = vitals[2], modifier = Modifier.weight(1f))
                    VitalCard(vital = vitals[3], modifier = Modifier.weight(1f))
                }
                VitalCard(vital = vitals[4], modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }

    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = {
                showNotificationDialog = false
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.bell_24),
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Bildirimler",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                }
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    if (notifications.isEmpty()) {
                        item {
                            Text(
                                text = "Henüz bildirim bulunmuyor",
                                fontFamily = Poppins,
                                color = colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        items(notifications) { notification ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = notification,
                                    fontFamily = Poppins,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorScheme.onSurface,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNotificationDialog = false
                    }
                ) {
                    Text(
                        text = "Tamam",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        notifications = emptyList()
                        showNotificationDialog = false
                    }
                ) {
                    Text(
                        text = "Tümünü Temizle",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.secondary
                    )
                }
            },
            containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
            shape = RoundedCornerShape(16.dp)
        )
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToChatbot = {},
        onNavigateToReports = {},
        onNavigateToProfile = {},
        onNavigateToCalendarAndNotes = {},
        onNavigateToAdmin = {}
    )
}