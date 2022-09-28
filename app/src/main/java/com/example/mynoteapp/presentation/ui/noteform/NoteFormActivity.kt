package com.example.mynoteapp.presentation.ui.noteform

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.mynoteapp.R
import com.example.mynoteapp.data.local.database.entity.NoteEntity
import com.example.mynoteapp.databinding.ActivityNoteFormBinding
import com.example.mynoteapp.di.ServiceLocator
import com.example.mynoteapp.utils.viewModelFactory
import com.example.mynoteapp.wrapper.Resource

class NoteFormActivity : AppCompatActivity() {

    private val noteId: Int? by lazy {
        intent?.getIntExtra(ARG_NOTE_ID, UNSET_NOTE_ID)
    }

    private val binding: ActivityNoteFormBinding by lazy {
        ActivityNoteFormBinding.inflate(layoutInflater)
    }

    private val viewModel: NoteFormViewModel by viewModelFactory {
        NoteFormViewModel(ServiceLocator.provideLocalRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        observeData()
        setOnClickListener()
        getInitialData()
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =
            if (isEditAction())
                getString(R.string.text_menu_edit_item)
            else
                getString(R.string.text_menu_create_item)
    }

    private fun isEditAction(): Boolean {
        return noteId != null && noteId != UNSET_NOTE_ID
    }

    private fun observeData() {
        viewModel.detailDataResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    //do nothing
                    setLoading(true)
                }
                is Resource.Success -> {
                    setLoading(false)
                    bindDataToForm(it.payload)
                }
                is Resource.Error -> {
                    setLoading(false)
                    finish()
                    Toast.makeText(this, "Error when get data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.updateResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    setFormEnabled(false)
                    setLoading(true)
                }
                is Resource.Success -> {
                    setFormEnabled(true)
                    setLoading(false)
                    finish()
                    Toast.makeText(this, "Update data Success", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    setFormEnabled(true)
                    setLoading(false)
                    finish()
                    Toast.makeText(this, "Error when get data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.insertResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    setFormEnabled(false)
                    setLoading(true)
                }
                is Resource.Success -> {
                    setFormEnabled(true)
                    setLoading(false)
                    finish()
                    Toast.makeText(this, "Add New data Success", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    setFormEnabled(true)
                    setLoading(false)
                    finish()
                    Toast.makeText(this, "Error when get data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.pbForm.isVisible = isLoading
        binding.clForm.isVisible = !isLoading
    }

    private fun setFormEnabled(isFormEnabled: Boolean) {
        with(binding) {
            tilTitle.isEnabled = isFormEnabled
            tilNote.isEnabled = isFormEnabled
            btnSave.isEnabled = isFormEnabled
        }
    }

    private fun bindDataToForm(data: NoteEntity?) {
        data?.let {
            binding.etTitle.setText(data.title)
            binding.etNote.setText(data.note)
        }
    }

    private fun setOnClickListener() {
        binding.btnSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        if (checkFormValidation()) {
            if (isEditAction()) {
                viewModel.updateNote(parseFormIntoEntity())
            } else {
                viewModel.insertNewNote(parseFormIntoEntity())
            }
        }
    }

    private fun checkFormValidation(): Boolean {
        val title = binding.etTitle.text.toString()
        val note = binding.etNote.text.toString()
        var isFormValid = true
        if (title.isEmpty()) {
            isFormValid = false
            binding.tilTitle.isErrorEnabled = true
            binding.tilTitle.error = getString(R.string.text_error_empty_title)
        } else {
            binding.tilTitle.isErrorEnabled = false
        }
        if (note.isEmpty()) {
            isFormValid = false
            binding.tilNote.isErrorEnabled = true
            binding.tilNote.error = getString(R.string.text_error_empty_note)
        } else {
            binding.tilNote.isErrorEnabled = false
        }
        return isFormValid
    }

    private fun getInitialData() {
        if (isEditAction()) {
            noteId?.let {
                viewModel.getNoteById(it)
            }
        }
    }

    private fun parseFormIntoEntity(): NoteEntity {
        return NoteEntity(
            title = binding.etTitle.text.toString(),
            note = binding.etNote.text.toString(),
        ).apply {
            if (isEditAction()) {
                noteId?.let {
                    id = it
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        private const val ARG_NOTE_ID = "ARG_NOTE_ID"
        private const val UNSET_NOTE_ID = -1

        @JvmStatic
        fun newInstance(context: Context, noteId: Int? = null): Intent =
            Intent(context, NoteFormActivity::class.java).apply {
                noteId?.let {
                    putExtra(ARG_NOTE_ID, it)
                }
            }
    }
}