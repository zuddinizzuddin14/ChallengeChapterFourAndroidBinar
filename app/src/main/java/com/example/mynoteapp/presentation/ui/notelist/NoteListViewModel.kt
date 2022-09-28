package com.example.mynoteapp.presentation.ui.notelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynoteapp.data.local.database.entity.NoteEntity
import com.example.mynoteapp.data.repository.LocalRepository
import com.example.mynoteapp.wrapper.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteListViewModel(private val repository: LocalRepository) : ViewModel() {

    val noteListResult = MutableLiveData<Resource<List<NoteEntity>>>()
    val deleteResult = MutableLiveData<Resource<Number>>()

    fun getUsername(): String? {
        return repository.getUsername()
    }

    fun checkIfUserIsExist(): Boolean {
        return repository.checkIfUserIsExist()
    }

    fun getSession(): Boolean {
        return repository.getSession()
    }

    fun sessionLogout() {
        return repository.setSession(false)
    }

    fun getNoteList() {
        viewModelScope.launch {
            noteListResult.postValue(Resource.Loading())
            delay(500)
            noteListResult.postValue(repository.getNoteList())
        }
    }

    fun deleteNote(item: NoteEntity) {
        viewModelScope.launch {
            deleteResult.postValue(Resource.Loading())
            delay(500)
            deleteResult.postValue(repository.deleteNote(item))
        }
    }
}