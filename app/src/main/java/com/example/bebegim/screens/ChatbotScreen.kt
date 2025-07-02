package com.example.bebegim.screens

import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bebegim.R
import com.example.bebegim.model.ChatMessage
import com.example.bebegim.model.MessageType
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.ui.theme.Poppins
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToThermalCamera: () -> Unit,
    onNavigateToCalendarAndNotes: () -> Unit
) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val inputText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isTyping = remember { mutableStateOf(false) }

    // Initial welcome message
    LaunchedEffect(Unit) {
        messages.add(
            ChatMessage(
                "Merhaba! Ben senin AI asistanınım. Bugün bebeğinizin bakımına nasıl yardımcı olabilirim?",
                MessageType.RECEIVED
            )
        )
    }
    val isDark = isSystemInDarkTheme()
    Scaffold(
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AI Asistan",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "chatbot",
                onHomeClick = onNavigateToHome,
                onChatClick = onNavigateToChatbot,
                onReportsClick = onNavigateToReports,
                onCalendarAndNotesClick = onNavigateToCalendarAndNotes,
                onThermalCameraClick = onNavigateToThermalCamera, // <-- Add this line
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = listState,
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message = message)
                }

                if (isTyping.value) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            // Scroll to bottom when new message is added
            LaunchedEffect(messages.size, isTyping.value) {
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }

            // Input Area
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        placeholder = {
                            Text("Herhangi bir şey sorun")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp)),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Send Button
                    IconButton(
                        onClick = {
                            if (inputText.value.isNotBlank()) {
                                val userMessage = inputText.value.trim()
                                messages.add(ChatMessage(userMessage, MessageType.SENT))
                                inputText.value = ""

                                // Simulate AI typing
                                isTyping.value = true

                                coroutineScope.launch {
                                    delay(1500)
                                    isTyping.value = false

                                    val response = when {
                                        userMessage.contains("sıcaklık", ignoreCase = true) ||
                                                userMessage.contains("oda sıcaklığı", ignoreCase = true) ->
                                            "Bebekler için ideal oda sıcaklığı 20-22°C (68-72°F) arasındadır. Bebeğinizi uygun şekilde giydirdiğinizden emin olun – ne çok sıcak ne de çok soğuk."

                                        userMessage.contains("beslenme", ignoreCase = true) ||
                                                userMessage.contains("emzirme", ignoreCase = true) ||
                                                userMessage.contains("mama", ignoreCase = true) ->
                                            "Yenidoğanlar genellikle her 2-3 saatte bir beslenir. Köklenme, emme hareketleri veya ellerini ağzına götürme gibi açlık belirtilerine dikkat edin."

                                        userMessage.contains("uyku", ignoreCase = true) ||
                                                userMessage.contains("uyuyor", ignoreCase = true) ->
                                            "Yenidoğanlar günde 14-17 saat aralıklı şekilde uyurlar. Güvenli bir uyku ortamı oluşturun: sert yatak, gevşek yatak örtüsü olmamalı ve bebeği sırt üstü yatırın."

                                        userMessage.contains("acil", ignoreCase = true) ||
                                                userMessage.contains("acil durum", ignoreCase = true) ||
                                                userMessage.contains("ambulans", ignoreCase = true) ->
                                            "Eğer acil bir durum yaşıyorsanız, lütfen hemen 112'yi arayın. Nefes alma güçlüğü, morarmış dudaklar veya tepkisizlik gibi durumlar acil müdahale gerektirir."

                                        else ->
                                            "\"$userMessage\" hakkında bir şey sorduğunuzu anlıyorum. Bu konuyla ilgili bebeğinizin bakımı hakkında daha özel bilgiler vermemi ister misiniz?"
                                    }

                                    messages.add(ChatMessage(response, MessageType.RECEIVED))
                                }
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.paper_plane_top_24),
                            contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.type == MessageType.SENT)
            Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.type == MessageType.SENT) 16.dp else 4.dp,
                        bottomEnd = if (message.type == MessageType.SENT) 4.dp else 16.dp
                    )
                )
                .background(
                    if (message.type == MessageType.SENT)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondaryContainer
                )
                .padding(16.dp)
        ) {
            Text(
                text = message.content,
                color = if (message.type == MessageType.SENT)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                val infiniteTransition = rememberInfiniteTransition(label = "typing")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = 1000
                            0.2f at 0
                            1f at 500
                            0.2f at 1000
                        },
                        initialStartOffset = StartOffset(index * 100)
                    ),
                    label = "typing dot"
                )

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.onSecondaryContainer
                                .copy(alpha = alpha)
                        )
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ChatbotScreenPreview() {
    ChatbotScreen(
        onNavigateBack = {},
        onNavigateToChatbot = {},
        onNavigateToHome = {},
        onNavigateToReports = {},
        onNavigateToThermalCamera = {},
        onNavigateToCalendarAndNotes = {}
    )
}