package com.example.mynoteapp.presentation.ui.noteform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynoteapp.data.local.database.entity.NoteEntity
import com.example.mynoteapp.data.repository.LocalRepository
import com.example.mynoteapp.wrapper.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteFormViewModel(private val repository: LocalRepository): ViewModel() {
    val detailDataResult = MutableLiveData<Resource<NoteEntity?>>()
    val insertResult = MutableLiveData<Resource<Number>>()
    val updateResult = MutableLiveData<Resource<Number>>()

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            detailDataResult.postValue(Resource.Loading())
            delay(500)
            detailDataResult.postValue(repository.getNoteById(id))
        }
    }

    fun insertNewNote(item: NoteEntity) {
        viewModelScope.launch {
            insertResult.postValue(Resource.Loading())
            delay(500)
            insertResult.postValue(repository.insertNote(item))
        }
    }

    fun updateNote(item: NoteEntity) {
        viewModelScope.launch {
            updateResult.postValue(Resource.Loading())
            delay(500)
            updateResult.postValue(repository.updateNote(item))
        }
    }
}