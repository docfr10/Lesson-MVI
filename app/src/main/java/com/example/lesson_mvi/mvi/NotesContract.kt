package com.example.lesson_mvi.mvi

data class NotesState(
    val text: String = "",
    val notes: List<String> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface NotesIntent {
    data object AddNote : NotesIntent
    data class ChangeText(val value: String) : NotesIntent
    data class DeleteNote(val index: Int) : NotesIntent
}

sealed interface NotesResult {
    data class TextChanged(val value: String) : NotesResult
    data class LoadingChanged(val value: Boolean) : NotesResult
    data class NoteAdded(val value: String) : NotesResult
    data class NoteDeleted(val index: Int) : NotesResult
}

sealed interface NotesEffect {
    data class ShowMessage(val message: String) : NotesEffect
}