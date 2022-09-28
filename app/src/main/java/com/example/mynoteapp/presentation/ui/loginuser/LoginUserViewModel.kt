package com.example.mynoteapp.presentation.ui.loginuser

import androidx.lifecycle.ViewModel
import com.example.mynoteapp.data.repository.LocalRepository

class LoginUserViewModel(private val repository: LocalRepository): ViewModel() {
    fun checkIsUserCorrect(username: String, password: String): Boolean {
        return repository.checkIsUserCorrect(username, password)
    }

    fun userSession(session: Boolean) {
        return repository.setSession(session)
    }
}