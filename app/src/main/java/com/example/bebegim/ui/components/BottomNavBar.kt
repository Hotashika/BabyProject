package com.example.bebegim.ui.components


import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.bebegim.R

@Composable
fun BottomNavBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onChatClick: () -> Unit,
    onReportsClick: () -> Unit,
    onCalendarAndNotesClick: () -> Unit,
    onThermalCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.home_24),
                contentDescription = "Home") },
            label = { Text("Ev\n" +
                    "") },
            selected = currentRoute == "home",
            onClick = onHomeClick
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.messages_24),
                contentDescription = "Chat") },
            label = { Text("Asistan\n" +
                    "") },
            selected = currentRoute == "chatbot",
            onClick = onChatClick
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.menu_burger_24),
                contentDescription = "Reports") },
            label = { Text("Detaylar\n" +
                    "") },
            selected = currentRoute == "reports",
            onClick = onReportsClick
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.journal_alt_24),
                    contentDescription = "Calendar") },
                label = { Text("Takvim\n& Notlar") },
                selected = currentRoute == "calendar_and_notes",
                onClick = onCalendarAndNotesClick
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.camera_24),
                    contentDescription = "thermal_camera") },
            label = { Text("Termal\nKamera") },
            selected = currentRoute == "thermal_camera",
            onClick = onThermalCameraClick
        )

    }
}
