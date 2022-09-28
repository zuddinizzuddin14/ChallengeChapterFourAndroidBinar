package com.example.mynoteapp.presentation.ui.notelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynoteapp.R
import com.example.mynoteapp.data.local.database.entity.NoteEntity
import com.example.mynoteapp.databinding.ActivityNoteListBinding
import com.example.mynoteapp.di.ServiceLocator
import com.example.mynoteapp.presentation.ui.loginuser.LoginUser
import com.example.mynoteapp.presentation.ui.loginuser.OnLoginListener
import com.example.mynoteapp.presentation.ui.noteform.NoteFormActivity
import com.example.mynoteapp.presentation.ui.notelist.adapter.NoteItemClickListener
import com.example.mynoteapp.presentation.ui.notelist.adapter.NoteListAdapter
import com.example.mynoteapp.presentation.ui.registeruser.OnRegisterListener
import com.example.mynoteapp.presentation.ui.registeruser.RegisterUser
import com.example.mynoteapp.utils.viewModelFactory
import com.example.mynoteapp.wrapper.Resource

class NoteListActivity : AppCompatActivity() {

    private val binding: ActivityNoteListBinding by lazy {
        ActivityNoteListBinding.inflate(layoutInflater)
    }

    private val viewModel: NoteListViewModel by viewModelFactory {
        NoteListViewModel(ServiceLocator.provideLocalRepository(this))
    }

    private val adapter: NoteListAdapter by lazy {
        NoteListAdapter(object : NoteItemClickListener {
            override fun onItemClicked(item: NoteEntity) {
                Toast.makeText(this@NoteListActivity, "Item Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteMenuClicked(item: NoteEntity) {
                viewModel.deleteNote(item)
            }

            override fun onEditMenuClicked(item: NoteEntity) {
                startActivity(NoteFormActivity.newInstance(this@NoteListActivity, item.id))
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initList()
        setOnClickListeners()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        getData()
        binding.root.isVisible = false
        if (viewModel.checkIfUserIsExist().not()) {
            showRegisterDialog (false) {
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                binding.root.isVisible = true
                onResume()
            }
        } else {
            if (viewModel.getSession().not()) {
                showLoginDialog(false) { isUserCorrect ->
                    if (isUserCorrect) {
                        binding.root.isVisible = true
                    }
                }
            } else {
                binding.root.isVisible = true
            }
        }
        binding.tvHello.text = "Hello ${viewModel.getUsername()}"
    }

    private fun initList() {
        binding.rvNoteList.apply {
            layoutManager = LinearLayoutManager(this@NoteListActivity)
            adapter = this@NoteListActivity.adapter
        }
    }

    private fun setOnClickListeners() {
        binding.fabAddData.setOnClickListener {
            startActivity(NoteFormActivity.newInstance(this))
        }
    }

    private fun observeData() {
        viewModel.noteListResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    setLoadingState(true)
                    setErrorState(false)
                }
                is Resource.Success -> {
                    setLoadingState(false)
                    setErrorState(false)
                    bindDataToAdapter(it.payload)
                }
                is Resource.Error -> {
                    setLoadingState(false)
                    setErrorState(true, it.exception?.message.orEmpty())
                }
            }
        }
        viewModel.deleteResult.observe(this) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Toast.makeText(this, "Delete Note Success", Toast.LENGTH_SHORT).show()
                    getData()
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Delete Note Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindDataToAdapter(data: List<NoteEntity>?) {
        if (data.isNullOrEmpty()) {
            adapter.clearItems()
            setErrorState(true, getString(R.string.text_error_note_empty))
        } else {
            adapter.setItems(data)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.pbNoteList.isVisible = isLoading
        binding.rvNoteList.isVisible = !isLoading
    }

    private fun setErrorState(isError: Boolean, errorMsg: String = "") {
        binding.tvError.text = errorMsg
        binding.tvError.isVisible = isError
    }

    private fun getData() {
        viewModel.getNoteList()
    }

    private fun showRegisterDialog(
        isCancelable: Boolean = true,
        onRegister: () -> Unit
    ) {
        val currentDialog =
            supportFragmentManager.findFragmentByTag(RegisterUser::class.java.simpleName)
        if (currentDialog == null) {
            RegisterUser().apply {
                setListener(object : OnRegisterListener {
                    override fun onRegister() {
                        onRegister.invoke()
                    }
                })
                this.isCancelable = isCancelable
            }.show(supportFragmentManager, RegisterUser::class.java.simpleName)
        }
    }

    private fun showLoginDialog(
        isCancelable: Boolean = true,
        onLogin: (Boolean) -> Unit
    ) {
        val currentDialog =
            supportFragmentManager.findFragmentByTag(LoginUser::class.java.simpleName)
        if (currentDialog == null) {
            LoginUser().apply {
                setListener(object : OnLoginListener {
                    override fun onLogin(isUserCorrect: Boolean) {
                        onLogin.invoke(isUserCorrect)
                    }
                })
                this.isCancelable = isCancelable
            }.show(supportFragmentManager, LoginUser::class.java.simpleName)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_logout -> {
                viewModel.sessionLogout()
                onResume()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}