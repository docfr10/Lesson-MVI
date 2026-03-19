package com.example.lesson_mvi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson_mvi.mvi.NotesEffect
import com.example.lesson_mvi.mvi.NotesIntent
import com.example.lesson_mvi.mvi.NotesReducer
import com.example.lesson_mvi.mvi.NotesResult
import com.example.lesson_mvi.mvi.NotesState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val _notesState = MutableStateFlow(value = NotesState())
    val notesState = _notesState.asStateFlow()

    private val _effect = MutableSharedFlow<NotesEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: NotesIntent) {
        when (intent) {
            is NotesIntent.AddNote -> addNote()

            is NotesIntent.ChangeText -> dispatch(result = NotesResult.TextChanged(value = intent.value))

            is NotesIntent.DeleteNote -> dispatch(result = NotesResult.NoteDeleted(index = intent.index))
        }
    }

    private fun addNote() {
        val text = notesState.value.text.trim()

        if (text.isBlank()) {
            sendEffect(NotesEffect.ShowMessage(message = "Введите текст заметки"))
            return
        }

        viewModelScope.launch {
            dispatch(result = NotesResult.LoadingChanged(value = true))
            delay(timeMillis = 300)
            dispatch(result = NotesResult.NoteAdded(value = text))
        }
    }

    private fun dispatch(result: NotesResult) {
        _notesState.value = NotesReducer.reduce(state = notesState.value, result = result)
    }

    private fun sendEffect(effect: NotesEffect) =
        viewModelScope.launch { _effect.emit(value = effect) }
}