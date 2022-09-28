package com.example.mynoteapp.data.repository

import com.example.mynoteapp.data.local.database.datasource.NoteDataSource
import com.example.mynoteapp.data.local.database.entity.NoteEntity
import com.example.mynoteapp.data.local.preference.UserPreferenceDataSource
import com.example.mynoteapp.wrapper.Resource

interface LocalRepository {

    fun checkIfUserIsExist(): Boolean
    fun checkIsUserCorrect(username: String, password: String): Boolean
    fun getSession(): Boolean
    fun getUsername(): String?
    fun setUser(newUsername: String, newPassword: String)
    fun setSession(newSession: Boolean)

    suspend fun getNoteList(): Resource<List<NoteEntity>>
    suspend fun getNoteById(id: Int): Resource<NoteEntity?>
    suspend fun insertNote(note: NoteEntity): Resource<Number>
    suspend fun updateNote(note: NoteEntity): Resource<Number>
    suspend fun deleteNote(note: NoteEntity): Resource<Number>
}

class LocalRepositoryImpl(
    private val userPreferenceDataSource: UserPreferenceDataSource,
    private val noteDataSource: NoteDataSource
) : LocalRepository {
    override fun checkIfUserIsExist(): Boolean {
        return (userPreferenceDataSource.getUsername().isNullOrEmpty().not() &&
                userPreferenceDataSource.getPassword().isNullOrEmpty().not())
    }

    override fun checkIsUserCorrect(username: String, password: String): Boolean {
        return (userPreferenceDataSource.getUsername().equals(username, ignoreCase = true) &&
                userPreferenceDataSource.getPassword().equals(password, ignoreCase = true))
    }

    override fun getSession(): Boolean {
        return userPreferenceDataSource.getSession()
    }

    override fun getUsername(): String? {
        return userPreferenceDataSource.getUsername()
    }

    override fun setUser(newUsername: String, newPassword: String) {
        userPreferenceDataSource.setUser(newUsername, newPassword)
    }

    override fun setSession(newSession: Boolean) {
        userPreferenceDataSource.setSession(newSession)
    }

    override suspend fun getNoteList(): Resource<List<NoteEntity>> {
        return proceed {
            noteDataSource.getAllNotes()
        }
    }

    override suspend fun getNoteById(id: Int): Resource<NoteEntity?> {
        return proceed {
            noteDataSource.getNoteById(id)
        }
    }

    override suspend fun insertNote(note: NoteEntity): Resource<Number> {
        return proceed {
            noteDataSource.insertNote(note)
        }
    }

    override suspend fun updateNote(note: NoteEntity): Resource<Number> {
        return proceed {
            noteDataSource.updateNote(note)
        }
    }

    override suspend fun deleteNote(note: NoteEntity): Resource<Number> {
        return proceed {
            noteDataSource.deleteNote(note)
        }
    }

    private suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutine.invoke())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

}