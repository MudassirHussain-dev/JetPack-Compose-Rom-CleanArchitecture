package com.example.jetpackcomposeroomcrud.feature_note.presentation.notes


import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposeroomcrud.feature_note.domain.model.Note
import com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.NoteUseCases
import com.example.jetpackcomposeroomcrud.feature_note.domain.util.NoteOrder
import com.example.jetpackcomposeroomcrud.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())

    val state: State<NotesState> = _state

    private var recentlyDeletableNotes: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {

        when (event) {

            is NotesEvent.Order -> {

                if (state.value.noteOrder == event.noteOrder &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType

                ) {
                    return
                }

                getNotes(event.noteOrder)
            }


            is NotesEvent.DeleteNote -> {

                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletableNotes = event.note
                }
            }


            is NotesEvent.RegisterNote -> {

                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletableNotes ?: return@launch)
                    recentlyDeletableNotes = null
                }
            }


            is NotesEvent.ToggleOrderSection -> {

                _state.value = _state.value.copy(
                    isOrderSectionVisible = !_state.value.isOrderSectionVisible
                )
            }

        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()

        getNotesJob = noteUseCases.getNotes(noteOrder)

            .onEach { notes ->
                _state.value = _state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )

            }.launchIn(viewModelScope)
    }
}