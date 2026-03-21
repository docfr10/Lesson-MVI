package com.example.lesson_mvi.mvi

// Это текущее состояние экрана
data class NotesState(
    val text: String = "", // Текст, который сейчас введен в поле
    val notes: List<String> = emptyList(), // Список всех заметок
    val isLoading: Boolean = false // Показывает, идет ли сейчас загрузка
)

// Это действия пользователя на экране
sealed interface NotesIntent {
    data object AddNote : NotesIntent // Пользователь нажал "Добавить"
    data class ChangeText(val value: String) : NotesIntent // Пользователь изменил текст
    data class DeleteNote(val index: Int) : NotesIntent // Пользователь удалил заметку по индексу
}

// Это результаты обработки действий
sealed interface NotesResult {
    data class TextChanged(val value: String) : NotesResult // Текст обновился
    data class LoadingChanged(val value: Boolean) : NotesResult // Состояние загрузки изменилось
    data class NoteAdded(val value: String) : NotesResult // Новая заметка добавлена
    data class NoteDeleted(val index: Int) : NotesResult // Заметка удалена
}

// Это одноразовые события для UI
sealed interface NotesEffect {
    data class ShowMessage(val message: String) : NotesEffect // Показать сообщение пользователю
}