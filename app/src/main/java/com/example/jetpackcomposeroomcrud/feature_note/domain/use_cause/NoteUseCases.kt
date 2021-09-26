package com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause

import com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.delete_note.DeleteNote

data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote
)
