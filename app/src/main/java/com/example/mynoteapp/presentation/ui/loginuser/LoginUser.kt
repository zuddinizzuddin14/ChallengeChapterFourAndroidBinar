package com.example.mynoteapp.presentation.ui.loginuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mynoteapp.R
import com.example.mynoteapp.databinding.FragmentLoginUserBinding
import com.example.mynoteapp.di.ServiceLocator
import com.example.mynoteapp.utils.viewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LoginUser : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLoginUserBinding
    private var listener : OnLoginListener? = null

    private val viewModel: LoginUserViewModel by viewModelFactory {
        LoginUserViewModel(ServiceLocator.provideLocalRepository(requireContext()))
    }

    fun setListener(listener: OnLoginListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginUserBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun loginUser() {
        if (validateForm()) {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val isUserCorrect = viewModel.checkIsUserCorrect(username, password)
            listener?.onLogin(isUserCorrect)
            if(isUserCorrect){
                viewModel.userSession(isUserCorrect)
                dismiss()
            }else{
                Toast.makeText(requireContext(), getString(R.string.error_text_login_not_correct), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setOnClickListeners() {
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun validateForm(): Boolean {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
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
        return isFormValid
    }
}

interface OnLoginListener{
    fun onLogin(isUserCorrect : Boolean)
}