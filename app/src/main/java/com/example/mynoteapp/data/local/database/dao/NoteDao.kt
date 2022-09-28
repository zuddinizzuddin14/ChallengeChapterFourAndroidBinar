package com.example.mynoteapp.data.local.database.dao

import androidx.room.*
import com.example.mynoteapp.data.local.database.entity.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM NOTES")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM NOTES WHERE id == :id LIMIT 1")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Delete
    suspend fun deleteNote(note: NoteEntity): Int

    @Update
    suspend fun updateNote(note: NoteEntity): Int

}