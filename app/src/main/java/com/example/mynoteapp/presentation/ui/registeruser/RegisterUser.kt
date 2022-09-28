package com.example.mynoteapp.presentation.ui.registeruser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mynoteapp.R
import com.example.mynoteapp.databinding.FragmentRegisterUserBinding
import com.example.mynoteapp.di.ServiceLocator
import com.example.mynoteapp.utils.viewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegisterUser: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRegisterUserBinding
    private var listener: OnRegisterListener? = null

    private val viewModel: RegisterUserViewModel by viewModelFactory {
        RegisterUserViewModel(ServiceLocator.provideLocalRepository(requireContext()))
    }

    fun setListener(listener: OnRegisterListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        if (validateForm()) {
            val newUsername = binding.etUsername.text.toString().trim()
            val newPassword = binding.etPassword.text.toString().trim()
            context?.let {
                viewModel.setUser(newUsername, newPassword)
            }
            listener?.onRegister()
            dismiss()
        }
    }

    private fun validateForm(): Boolean {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmedPassword = binding.etConfirmPassword.text.toString()
        var isFormValid = true

        if (username.isEmpty()) {
            isFormValid = false
            binding.tilUsername.isErrorEnabled = true
            binding.tilUsername.error = getString(R.string.error_text_empty_username)
        } else {
            binding.tilUsername.isErrorEnabled = false
        }
        if (password.isEmpty()) {
            isFormValid = false
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = getString(R.string.error_text_empty_password)
        } else {
            binding.tilPassword.isErrorEnabled = false
        }
        if (confirmedPassword.isEmpty()) {
            isFormValid = false
            binding.tilConfirmPassword.isErrorEnabled = true
            binding.tilConfirmPassword.error = getString(R.string.error_text_empty_confirmed_password)
        } else {
            binding.tilConfirmPassword.isErrorEnabled = false
        }
        if (password != confirmedPassword) {
            isFormValid = false
            Toast.makeText(
                context,
                getString(R.string.error_password_not_match),
                Toast.LENGTH_SHORT
            ).show()
        }
        return isFormValid
    }
}

interface OnRegisterListener{
    fun onRegister()
}
