package com.example.bebegim.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bebegim.data.GetThermalData
import com.example.bebegim.data.ListData
import com.example.bebegim.ui.components.BarChart
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.components.LineChart
import kotlinx.coroutines.delay

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
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
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

    // Temp Data
    LaunchedEffect(Unit) {
        val getThermalData = GetThermalData()
        while (true) {
            val temp = getThermalData.fetchMeanTemperature()
            temp?.let { listData.addTemperature(it) }
            delay(2000) // 3h = 10800s
        }
    }

    val temps = listData.getTemperatures()


    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Bebeğin Sıcaklığı (Bugün)",
                    style = MaterialTheme.typography.titleMedium
                )

                LineChart(
                    data = temps,
                    labels = listOf("06:00", "09:00", "12:00", "15:00", "18:00", "21:00", "00:00"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    lineColor = Color(0xFF4CAF50),
                    pointColor = Color(0xFF388E3C),
                    showGrid = true,
                    showValuesOnPoints = true,
                    fixedYAxisRange = 20f to 40f,
                    curvedLine = true
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Şimdi: ${temps.lastOrNull()?.let { "%.1f°C".format(it) } ?: "-"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Min: ${temps.minOrNull()?.let { "%.1f°C".format(it) } ?: "-"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Max: ${temps.maxOrNull()?.let { "%.1f°C".format(it) } ?: "-"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Ortalama: ${
                            if (temps.isNotEmpty()) "%.1f°C".format(temps.average()) else "-"
                        }",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
        }

        // Room Info
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Oda Koşulları (Bugün)",
                    style = MaterialTheme.typography.titleMedium
                )

                BarChart(
                    data = listOf(
                        Pair("Sıcaklık (°C)", 22f),
                        Pair("Nem (%)", 45f),
                        Pair("Gürültü (dB)", 35f)
                    ),
                    maxValue = 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

@Composable
fun WeeklyReportContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Weekly Temp Trends
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Haftalık Sıcaklık Trendleri",
                    style = MaterialTheme.typography.titleMedium
                )

                LineChart(
                    data = listOf(
                        21.4f,
                        21.5f,
                        21.5f,
                        21.5f,
                        21.6f,
                        21.6f,
                        21.7f
                    ),
                    labels = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cts", "Paz"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    lineColor = Color(0xFF4CAF50),
                    pointColor = Color(0xFF388E3C),
                    showGrid = true,
                    showValuesOnPoints = true,
                    fixedYAxisRange = 0f to 30f,
                    curvedLine = true
                )
            }
        }

        // Weekly Sleep Data
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Haftalık Uyku Düzeni",
                    style = MaterialTheme.typography.titleMedium
                )

                BarChart(
                    data = listOf(
                        Pair("Pzt", 14f),
                        Pair("Sal", 15f),
                        Pair("Çar", 13f),
                        Pair("Per", 16f),
                        Pair("Cum", 14f),
                        Pair("Cts", 15f),
                        Pair("Paz", 14f)
                    ),
                    maxValue = 24f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Text(
                    text = "Ortalama uyku: 14.4 saat/gün",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Weekly Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Haftalık Özet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "Bebeğiniz sağlıklı bir hafta geçirdi, yaşamsal bulgular stabil. Uyku düzeni yaşa uygun ve tutarlı.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun MonthlyReportContent() {
    val growthData = listOf(4.2f, 4.3f, 4.5f, 4.6f, 4.8f, 4.9f, 5.1f)
    val labels = listOf("1.Hafta", "2.Hafta", "3.Hafta", "4.Hafta", "5.Hafta", "6.Hafta", "7.Hafta")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Monthly Growth Chart
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Aylık Büyüme Grafiği",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                LineChart(
                    data = growthData,
                    labels = labels,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    lineColor = Color(0xFF4CAF50),
                    pointColor = Color(0xFF388E3C),
                    showGrid = true,
                    showValuesOnPoints = true,
                    fixedYAxisRange = 0f to 15f,
                    curvedLine = true
                )

                Text(
                    text = "Bu ay toplamda 0.9 kg kilo alındı",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }


        // Monthly Healt Summary
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Aylık Sağlık Özeti",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HealthStat(title = "Ortalama Ateş", value = "36.5°C")
                    HealthStat(title = "Ortalama Uyku", value = "14.2 sa/gün")
                    HealthStat(title = "Sağlık Uyarısı", value = "2 hafif")
                }
            }
        }
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
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