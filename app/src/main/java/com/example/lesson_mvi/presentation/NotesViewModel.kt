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

// ViewModel хранит состояние экрана и обрабатывает действия пользователя
class NotesViewModel : ViewModel() {
    // Внутренний изменяемый state
    private val _notesState = MutableStateFlow(value = NotesState())

    // Внешний state только для чтения
    val notesState = _notesState.asStateFlow()

    // Поток одноразовых событий, например сообщений
    private val _effect = MutableSharedFlow<NotesEffect>()

    // Внешний effect только для чтения
    val effect = _effect.asSharedFlow()

    // Сюда приходят все действия пользователя
    fun onIntent(intent: NotesIntent) {
        when (intent) {
            // Если нажали кнопку добавления заметки
            is NotesIntent.AddNote -> addNote()
            // Если изменили текст в поле ввода
            is NotesIntent.ChangeText ->
                dispatch(result = NotesResult.TextChanged(value = intent.value))
            // Если удалили заметку
            is NotesIntent.DeleteNote ->
                dispatch(result = NotesResult.NoteDeleted(index = intent.index))
        }
    }

    // Добавление заметки
    private fun addNote() {
        // Убираем лишние пробелы в начале и в конце
        val text = notesState.value.text.trim()

        // Если текст пустой, показываем сообщение
        if (text.isBlank()) {
            sendEffect(NotesEffect.ShowMessage(message = "Введите текст заметки"))
            return
        }

        // Запускаем корутину
        viewModelScope.launch {
            // Включаем состояние загрузки
            dispatch(result = NotesResult.LoadingChanged(value = true))

            // Небольшая задержка для примера
            delay(timeMillis = 300)

            // Добавляем заметку
            dispatch(result = NotesResult.NoteAdded(value = text))
        }
    }

    // Передаем результат в reducer, чтобы получить новое состояние
    private fun dispatch(result: NotesResult) {
        _notesState.value = NotesReducer.reduce(
            state = notesState.value,
            result = result
        )
    }

    // Отправляем одноразовое событие в UI
    private fun sendEffect(effect: NotesEffect) =
        viewModelScope.launch { _effect.emit(value = effect) }
}