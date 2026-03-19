package com.example.lesson_mvi.mvi

object NotesReducer {
    fun reduce(state: NotesState, result: NotesResult): NotesState {
        return when (result) {
            is NotesResult.TextChanged -> state.copy(text = result.value)

            is NotesResult.LoadingChanged -> state.copy(isLoading = result.value)

            is NotesResult.NoteAdded -> state.copy(
                text = "",
                isLoading = false,
                notes = state.notes + result.value
            )

            is NotesResult.NoteDeleted -> {
                if (result.index !in state.notes.indices) return state

                state.copy(
                    notes = state.notes.toMutableList().apply {
                        removeAt(result.index)
                    }
                )
            }
        }
    }
}