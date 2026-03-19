package com.example.lesson_mvi.mvi

// Reducer отвечает за обновление состояния
object NotesReducer {
    // Функция принимает старое состояние и результат, а возвращает новое состояние
    fun reduce(state: NotesState, result: NotesResult): NotesState {
        return when (result) {
            // Если изменился текст, обновляем только поле text
            is NotesResult.TextChanged -> state.copy(text = result.value)

            // Если изменилось состояние загрузки, обновляем только isLoading
            is NotesResult.LoadingChanged -> state.copy(isLoading = result.value)

            // Если добавили заметку очищаем поле ввода, выключаем загрузку и добавляем новую заметку в список
            is NotesResult.NoteAdded -> state.copy(
                text = "",
                isLoading = false,
                notes = state.notes + result.value
            )

            // Если нужно удалить заметку
            is NotesResult.NoteDeleted -> {

                // Проверяем, есть ли такой индекс в списке
                // Если нет — просто возвращаем текущее состояние
                if (result.index !in state.notes.indices) return state

                // Если индекс корректный, создаем новый список без удаленной заметки
                state.copy(
                    notes = state.notes.toMutableList().apply {
                        removeAt(result.index)
                    }
                )
            }
        }
    }
}