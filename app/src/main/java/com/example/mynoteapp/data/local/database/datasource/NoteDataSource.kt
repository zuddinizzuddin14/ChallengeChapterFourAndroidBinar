package com.example.mynoteapp.data.local.database.datasource

import com.example.mynoteapp.data.local.database.dao.NoteDao
import com.example.mynoteapp.data.local.database.entity.NoteEntity

interface NoteDataSource {
    suspend fun getAllNotes(): List<NoteEntity>

    suspend fun getNoteById(id: Int): NoteEntity?

    suspend fun insertNote(note: NoteEntity): Long

    suspend fun deleteNote(note: NoteEntity): Int

    suspend fun updateNote(note: NoteEntity): Int
}

class NoteDataSourceImpl(private val noteDao: NoteDao) : NoteDataSource {
    override suspend fun getAllNotes(): List<NoteEntity> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNoteById(id: Int): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: NoteEntity): Int {
        return noteDao.deleteNote(note)
    }

    override suspend fun updateNote(note: NoteEntity): Int {
        return noteDao.updateNote(note)
    }

}
