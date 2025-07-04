package com.example.bebegim.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bebegim.data.Note
import com.example.bebegim.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class NotesViewModelFactory(
    private val repository: NoteRepository,
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class NotesViewModel(
    private val repository: NoteRepository,
    private val userId: String
) : ViewModel() {
    private val _notes = MutableStateFlow<Map<LocalDate, String>>(emptyMap())
    val notes: StateFlow<Map<LocalDate, String>> = _notes

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            val notesList = repository.getAllNotesForUser(userId)
            _notes.value = notesList.associate { it.date to it.content }
        }
    }

    fun saveNote(date: LocalDate, content: String) {
        viewModelScope.launch {
            repository.insertNote(Note(userId, date, content))
            loadNotes()
        }
    }

    fun deleteNoteByDate(date: LocalDate) {
        viewModelScope.launch {
            repository.deleteNoteByUserAndDate(userId, date)
            loadNotes()
        }
    }
}