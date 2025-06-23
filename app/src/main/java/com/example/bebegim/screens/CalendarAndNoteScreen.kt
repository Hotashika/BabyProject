package com.example.bebegim.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite

@Composable
fun CalendarAndNoteScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()

    Scaffold(
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
        bottomBar = {
            BottomNavBar(
                currentRoute = "calendar_and_notes",
                onChatClick = onNavigateToChatbot,
                onReportsClick = onNavigateToReports,
                onCalendarAndNotesClick = { },
                onProfileClick = onNavigateToProfile,
                onHomeClick = onNavigateBack,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Calendar and Notes Screen",
                color = if (isDark) Color.White else Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun CalendarAndNoteScreenPreview() {
    CalendarAndNoteScreen(
        onNavigateBack = {},
        onNavigateToChatbot = {},
        onNavigateToReports = {},
        onNavigateToProfile = {}
    )
}