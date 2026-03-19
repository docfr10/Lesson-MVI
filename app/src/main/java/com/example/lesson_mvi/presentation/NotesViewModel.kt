package com.example.lesson_mvi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson_mvi.mvi.NotesEffect
import com.example.lesson_mvi.mvi.NotesIntent
import com.example.lesson_mvi.mvi.NotesState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val _state = MutableStateFlow(value = NotesState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NotesEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: NotesIntent) {
        when (intent) {
            is NotesIntent.ChangeText -> reduce { copy(text = intent.value) }

            is NotesIntent.AddNote -> addNote()

            is NotesIntent.DeleteNote -> {
                val current = _state.value.notes.toMutableList()

                if (intent.index in current.indices) {
                    current.removeAt(intent.index)
                    reduce { copy(notes = current) }
                }
            }
        }
    }

    private fun addNote() {
        val text = _state.value.text.trim()

        if (text.isBlank()) {
            sendEffect(NotesEffect.ShowMessage(message = "Введите текст заметки"))
            return
        }

        viewModelScope.launch {
            reduce { copy(isLoading = true) }

            // Искусственная задержка для демонстрации состояния загрузки
            delay(timeMillis = 300)

            reduce {
                copy(
                    isLoading = false,
                    text = "",
                    notes = notes + text
                )
            }
        }
    }

    private fun reduce(block: NotesState.() -> NotesState) {
        _state.value = _state.value.block()
    }

    private fun sendEffect(effect: NotesEffect) =
        viewModelScope.launch { _effect.emit(value = effect) }

}