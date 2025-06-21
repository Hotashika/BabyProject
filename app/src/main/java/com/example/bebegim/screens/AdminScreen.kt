package com.example.bebegim.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bebegim.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onNavigateBack: () -> Unit
) {
    val users = remember {
        listOf(
            UserProfile(
                id = "1",
                name = "Ayşe Yılmaz",
                email = "ayse@example.com",
                role = "Veli",
                babyName = "Mehmet",
                babyAge = "3 ay"
            ),
            UserProfile(
                id = "2",
                name = "Ali Demir",
                email = "ali@example.com",
                role = "Veli",
                babyName = "Zeynep",
                babyAge = "6 ay"
            ),
            UserProfile(
                id = "3",
                name = "Fatma Kaya",
                email = "fatma@example.com",
                role = "Veli",
                babyName = "Ahmet",
                babyAge = "2 ay"
            ),
            UserProfile(
                id = "4",
                name = "Mehmet Öz",
                email = "mehmet@example.com",
                role = "Doktor",
                babyName = null,
                babyAge = null
            )
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Kullanıcılar", "Sistem Kayıtları", "Analizler")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yönetici Paneli") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Satırı
            TabRow(
                selectedTabIndex = selectedTab
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Tab İçeriği
            when (selectedTab) {
                0 -> UsersTab(users)
                1 -> SystemLogsTab()
                2 -> AnalyticsTab()
            }
        }
    }
}

@Composable
fun UsersTab(users: List<UserProfile>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Kayıtlı Kullanıcılar",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserCard(user = user)
            }
        }
    }
}

@Composable
fun UserCard(user: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Row {
                    IconButton(onClick = { /* TODO: Kullanıcıyı düzenle */ }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Kullanıcıyı Düzenle"
                        )
                    }

                    IconButton(onClick = { /* TODO: Kullanıcıyı sil */ }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Kullanıcıyı Sil",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rol: ${user.role}",
                    style = MaterialTheme.typography.bodySmall
                )

                if (user.babyName != null && user.babyAge != null) {
                    Text(
                        text = "Bebek: ${user.babyName}, ${user.babyAge}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun SystemLogsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sistem Kayıtları",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Son Aktiviteler",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(10) { index ->
                        LogItem(
                            timestamp = "2023-05-${30 - index} ${10 + index}:${10 + index}:00",
                            message = when (index % 3) {
                                0 -> "Kullanıcı giriş yaptı: ayse@example.com"
                                1 -> "Yapay Zeka Sohbeti etkileşimi: Sıcaklık sorgusu"
                                else -> "Uyarı tetiklendi: Oda sıcaklığı yüksek"
                            },
                            level = when (index % 4) {
                                0 -> "BİLGİ"
                                1 -> "UYARI"
                                2 -> "HATA"
                                else -> "AYRINTI"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LogItem(
    timestamp: String,
    message: String,
    level: String
) {
    val color = when (level) {
        "HATA" -> MaterialTheme.colorScheme.error
        "UYARI" -> MaterialTheme.colorScheme.tertiary
        "BİLGİ" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(140.dp)
        )

        Text(
            text = level,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            modifier = Modifier.width(80.dp)
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Divider(modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun AnalyticsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sistem Analizleri",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Kullanım İstatistikleri
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Kullanım İstatistikleri",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatCard(
                        title = "Aktif Kullanıcılar",
                        value = "24",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    StatCard(
                        title = "Sohbet Sorguları",
                        value = "156",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    StatCard(
                        title = "Oluşturulan Uyarılar",
                        value = "12",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Yapay Zeka Performansı
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Yapay Zeka Performansı",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PerformanceItem(
                        title = "Yanıt Doğruluğu",
                        percentage = 94
                    )

                    PerformanceItem(
                        title = "Uyarı Hassasiyeti",
                        percentage = 89
                    )

                    PerformanceItem(
                        title = "Kullanıcı Memnuniyeti",
                        percentage = 92
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun PerformanceItem(
    title: String,
    percentage: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LinearProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            color = when {
                percentage >= 90 -> MaterialTheme.colorScheme.primary
                percentage >= 70 -> MaterialTheme.colorScheme.tertiary
                else -> MaterialTheme.colorScheme.error
            }
        )
    }
}
