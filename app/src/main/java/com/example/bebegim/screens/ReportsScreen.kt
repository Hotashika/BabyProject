package com.example.bebegim.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bebegim.data.GetThermalData
import com.example.bebegim.data.ListData
import com.example.bebegim.ui.components.BarChart
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.components.LineChart
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCalendarAndNotes: () -> Unit,
    onNavigateToThermalCamera: () -> Unit,
    onNavigateToChatbot: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Günlük", "Haftalık", "Aylık")

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "reports",
                onHomeClick = onNavigateBack,
                onChatClick = onNavigateToChatbot,
                onCalendarAndNotesClick = onNavigateToCalendarAndNotes,
                onReportsClick = {},
                onThermalCameraClick = onNavigateToThermalCamera
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedTab) {
                    0 -> DailyReportContent()
                    1 -> WeeklyReportContent()
                    2 -> MonthlyReportContent()
                }
            }
        }
    }
}

@Composable
fun DailyReportContent() {
    val listData = remember { ListData() }
    var roomTemp by remember { mutableStateOf(22f) }
    var humidity by remember { mutableStateOf(45f) }
    var noise by remember { mutableStateOf(35f) }

    // Gerçek zamanlı sıcaklık verisi
    LaunchedEffect(Unit) {
        val getThermalData = GetThermalData()
        while (true) {
            val temp = getThermalData.fetchMeanTemperature()
            temp?.let { listData.addTemperature(it) }

            // Oda koşullarını da güncelle
            roomTemp = 20f + Random.nextFloat() * 8f // 20-28°C arası
            humidity = 40f + Random.nextFloat() * 20f // 40-60% arası
            noise = 30f + Random.nextFloat() * 20f // 30-50dB arası

            delay(3000) // 3 saniyede bir güncelle
        }
    }

    val temps = listData.getTemperatures()
    val currentTemp = temps.lastOrNull() ?: 36.5f

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        // Anlık Durum Kartı
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    currentTemp > 37.5f -> Color(0xFFFFEBEE)
                    currentTemp < 36f -> Color(0xFFE3F2FD)
                    else -> Color(0xFFE8F5E8)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Şu Anki Durum",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "%.1f°C".format(currentTemp),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        currentTemp > 37.5f -> Color(0xFFD32F2F)
                        currentTemp < 36f -> Color(0xFF1976D2)
                        else -> Color(0xFF388E3C)
                    }
                )

                Text(
                    text = when {
                        currentTemp > 37.5f -> "Yüksek Ateş"
                        currentTemp < 36f -> "Düşük Sıcaklık"
                        else -> "Normal Sıcaklık"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Sıcaklık Grafiği
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Bebeğin Sıcaklığı (Bugün)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (temps.isNotEmpty()) {
                    LineChart(
                        data = temps,
                        labels = listOf("06:00", "09:00", "12:00", "15:00", "18:00", "21:00", "00:00"),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        lineColor = Color(0xFF4CAF50),
                        pointColor = Color(0xFF2E7D32),
                        showGrid = true,
                        showValuesOnPoints = true,
                        fixedYAxisRange = 32f to 40f,
                        curvedLine = true
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Veri yükleniyor...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // İstatistikler
                if (temps.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard(
                            title = "Minimum",
                            value = "%.1f°C".format(temps.minOrNull() ?: 0f),
                            color = Color(0xFF2196F3)
                        )
                        StatCard(
                            title = "Maksimum",
                            value = "%.1f°C".format(temps.maxOrNull() ?: 0f),
                            color = Color(0xFFFF5722)
                        )
                        StatCard(
                            title = "Ortalama",
                            value = "%.1f°C".format(temps.average()),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }
        }

        // Oda Koşulları
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Oda Koşulları (Canlı)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                BarChart(
                    data = listOf(
                        Pair("Sıcaklık", roomTemp),
                        Pair("Nem", humidity),
                        /*Pair("Gürültü", noise)*/
                    ),
                    maxValue = 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RoomStatCard("Oda Sıcaklığı", "%.1f°C".format(roomTemp))
                    RoomStatCard("Nem Oranı", "%.0f%%".format(humidity))
                    /*RoomStatCard("Gürültü", "%.0f dB".format(noise))
                */}
            }
        }
    }
}

@Composable
fun WeeklyReportContent() {
    // Daha dinamik haftalık veriler
    val weeklyTemps = remember {
        listOf(36.4f, 36.5f, 36.7f, 36.6f, 36.8f, 36.5f, 36.6f)
    }
    val sleepHours = remember {
        listOf(14.2f, 15.1f, 13.8f, 16.2f, 14.5f, 15.3f, 14.0f)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Haftalık Sıcaklık Trendleri
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Haftalık Sıcaklık Trendleri",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                LineChart(
                    data = weeklyTemps,
                    labels = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cts", "Paz"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    lineColor = Color(0xFF4CAF50),
                    pointColor = Color(0xFF2E7D32),
                    showGrid = true,
                    showValuesOnPoints = true,
                    fixedYAxisRange = 34f to 38.5f,
                    curvedLine = true
                )

                Text(
                    text = "Ortalama: %.1f°C | En yüksek: %.1f°C | En düşük: %.1f°C".format(
                        weeklyTemps.average(),
                        weeklyTemps.maxOrNull() ?: 0f,
                        weeklyTemps.minOrNull() ?: 0f
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Haftalık Uyku Verileri
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Haftalık Uyku Düzeni",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                BarChart(
                    data = sleepHours.mapIndexed { index, hours ->
                        Pair(listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cts", "Paz")[index], hours)
                    },
                    maxValue = 18f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Text(
                    text = "Ortalama uyku: %.1f saat/gün | Toplam: %.1f saat".format(
                        sleepHours.average(),
                        sleepHours.sum()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Haftalık Özet
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Haftalık Sağlık Özeti",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "✅ Bebeğiniz sağlıklı bir hafta geçirdi",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "✅ Vücut sıcaklığı normal aralıkta stabil",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "✅ Uyku düzeni yaşa uygun ve tutarlı",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun MonthlyReportContent() {
    val growthData = remember {
        listOf(4.2f, 4.3f, 4.5f, 4.6f, 4.8f, 4.9f, 5.1f)
    }
    val labels = listOf("1.Hafta", "2.Hafta", "3.Hafta", "4.Hafta")

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Aylık Büyüme Grafiği
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Aylık Büyüme Grafiği",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                LineChart(
                    data = growthData.take(4), // İlk 4 haftayı göster
                    labels = labels,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    lineColor = Color(0xFF4CAF50),
                    pointColor = Color(0xFF2E7D32),
                    showGrid = true,
                    showValuesOnPoints = true,
                    fixedYAxisRange = 3f to 6f,
                    curvedLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GrowthStatCard("Başlangıç", "%.1f kg".format(growthData.first()))
                    GrowthStatCard("Güncel", "%.1f kg".format(growthData[3]))
                    GrowthStatCard("Artış", "+%.1f kg".format(growthData[3] - growthData.first()))
                }
            }
        }

        // Aylık Sağlık Özeti
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Aylık Sağlık Özeti",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HealthStat(title = "Ortalama Sıcaklık", value = "36.5°C")
                    HealthStat(title = "Ortalama Uyku", value = "14.2 sa/gün")
                    HealthStat(title = "Sağlık Uyarısı", value = "Yok")
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E8)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🎉 Tebrikler! Bu ay bebeğinizin gelişimi mükemmel. Tüm parametreler ideal aralıkta.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                textAlign = TextAlign.Center
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RoomStatCard(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun GrowthStatCard(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun HealthStat(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CheckboxItem(
    text: String,
    checked: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}