package com.example.lesson_mvi.mvi

data class NotesState(
    val text: String = "",
    val notes: List<String> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface NotesIntent {
    data class ChangeText(val value: String) : NotesIntent
    data class DeleteNote(val index: Int) : NotesIntent
    data object AddNote : NotesIntent
}

sealed interface NotesEffect {
    data class ShowMessage(val message: String) : NotesEffect
}