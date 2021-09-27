package com.example.jetpackcomposeroomcrud.di

import android.app.Application
import androidx.room.Room
import com.example.jetpackcomposeroomcrud.feature_note.data.data_source.NoteDatabase
import com.example.jetpackcomposeroomcrud.feature_note.data.repository.NoteRepositoryImpl
import com.example.jetpackcomposeroomcrud.feature_note.domain.repository.NoteRepository
import com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.AddNote
import com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.GetNote
import com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.GetNotes
import com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.NoteUseCases
import com.example.jetpackcomposeroomcrud.feature_note.domain.use_cause.delete_note.DeleteNote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUserCase(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            GetNotes(repository),
            DeleteNote(repository),
            AddNote(repository),
            GetNote(repository)
        )
    }
}