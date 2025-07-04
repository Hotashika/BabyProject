package com.example.bebegim.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.viewModel.NotesViewModelFactory
import com.example.bebegim.viewmodel.NotesViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarAndNoteScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToThermalCamera: () -> Unit,
    notesViewModel: NotesViewModel? = null,
    userId: String = "testUser" // Buraya gerçek userId'yi verin
) {
    val isDark = isSystemInDarkTheme()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var currentNote by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current
//    val db = remember { com.example.bebegim.data.AppDatabase.getDatabase(context) }
//    val repository = remember { NoteRepository(db.noteDao()) }
//    val factory = remember { NotesViewModelFactory(repository, userId) }
//    val viewModel: NotesViewModel = notesViewModel ?: viewModel(factory = factory)
//    val notes by viewModel.notes.collectAsState()

//    LaunchedEffect(selectedDate, notes) {
//        selectedDate?.let { date ->
//            currentNote = notes[date] ?: ""
//            isEditing = false
//        }
//    }

    Scaffold(
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
        bottomBar = {
            BottomNavBar(
                currentRoute = "calendar_and_notes",
                onChatClick = onNavigateToChatbot,
                onReportsClick = onNavigateToReports,
                onCalendarAndNotesClick = { },
                onHomeClick = onNavigateBack,
                onThermalCameraClick = onNavigateToThermalCamera,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Calendar Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { currentMonth = currentMonth.minusMonths(1) }
                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Önceki ay",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }

                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("tr"))),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color.Black
                )

                IconButton(
                    onClick = { currentMonth = currentMonth.plusMonths(1) }
                ) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Sonraki ay",
                        tint = if (isDark) Color.White else Color.Black
                    )
                }
            }

            // Days of week header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val daysOfWeek = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                isDark = isDark,
//                noteDates = notes.keys.toSet()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Selected date info and note editor
            selectedDate?.let { date ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.8f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Seçilen Tarih",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDark) Color.White else Color.Black
                                )
                                Text(
                                    text = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else Color.Black,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            if (isEditing) {
                                Row {
                                    IconButton(
                                        onClick = {
//                                            if (currentNote.isBlank()) {
//                                                viewModel.deleteNoteByDate(date)
//                                            } else {
//                                                viewModel.saveNote(date, currentNote)
//                                            }
                                            isEditing = false
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Save,
                                            contentDescription = "Kaydet",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

//                                    if (notes.containsKey(date)) {
//                                        IconButton(
//                                            onClick = {
//                                                viewModel.deleteNoteByDate(date)
//                                                currentNote = ""
//                                                isEditing = false
//                                            }
//                                        ) {
//                                            Icon(
//                                                Icons.Default.Delete,
//                                                contentDescription = "Sil",
//                                                tint = MaterialTheme.colorScheme.error
//                                            )
//                                        }
//                                    }
                                }
                            } else {
                                TextButton(
                                    onClick = { isEditing = true }
                                ) {
//                                    Text(
//                                        text = if (notes.containsKey(date)) "Düzenle" else "Not Ekle",
//                                        color = MaterialTheme.colorScheme.primary
//                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isEditing) {
                            OutlinedTextField(
                                value = currentNote,
                                onValueChange = { currentNote = it },
                                label = { Text("Notunuzu yazın...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = if (isDark) Color.White else Color.Black,
                                    unfocusedTextColor = if (isDark) Color.White else Color.Black,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
                                ),
                                maxLines = 4
                            )
                        } else {
                            if (currentNote.isNotEmpty()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = currentNote,
                                        fontSize = 14.sp,
                                        color = if (isDark) Color.White else Color.Black,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = "Bu tarih için not ekleyebilir veya etkinlik planlayabilirsiniz.",
                                    fontSize = 14.sp,
                                    color = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f),
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
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
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    isDark: Boolean,
    noteDates: Set<LocalDate> = emptySet()
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    val calendarDates = mutableListOf<LocalDate?>()
    repeat(firstDayOfWeek) { calendarDates.add(null) }
    for (day in 1..daysInMonth) calendarDates.add(currentMonth.atDay(day))

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(calendarDates) { date ->
            CalendarDayItem(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == LocalDate.now(),
                hasNote = date != null && noteDates.contains(date),
                onDateSelected = onDateSelected,
                isDark = isDark
            )
        }
    }
}

@Composable
fun CalendarDayItem(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    hasNote: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    isDark: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .then(
                if (date != null) Modifier.clickable { onDateSelected(date) } else Modifier
            )
            .background(
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (isToday && !isSelected) 1.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        date?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = it.dayOfMonth.toString(),
                    color = when {
                        isSelected -> Color.White
                        else -> if (isDark) Color.White else Color.Black
                    },
                    fontSize = 14.sp,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                )
                if (hasNote && !isSelected) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }
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
        onNavigateToThermalCamera = {},
        userId = "testUser"
    )
}