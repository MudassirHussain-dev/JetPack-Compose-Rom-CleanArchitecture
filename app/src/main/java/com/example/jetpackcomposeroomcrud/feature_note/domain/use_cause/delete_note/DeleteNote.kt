package com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.delete_note

import com.example.jetpackcomposeroomcrud.feature_note.domain.model.Note
import com.example.jetpackcomposeroomcrud.feature_note.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        return repository.deleteNote(note)
    }
}