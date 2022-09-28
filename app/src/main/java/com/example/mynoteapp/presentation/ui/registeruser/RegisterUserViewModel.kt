package com.example.mynoteapp.presentation.ui.registeruser

import androidx.lifecycle.ViewModel
import com.example.mynoteapp.data.repository.LocalRepository


class RegisterUserViewModel(private val repository: LocalRepository) : ViewModel() {
    fun setUser(newUsername: String, newPassword: String){
        repository.setUser(newUsername, newPassword)
    }
}