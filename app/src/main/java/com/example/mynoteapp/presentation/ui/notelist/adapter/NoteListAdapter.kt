package com.example.mynoteapp.presentation.ui.notelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mynoteapp.R
import com.example.mynoteapp.data.local.database.entity.NoteEntity
import com.example.mynoteapp.databinding.ItemNoteBinding

class NoteListAdapter(
    private val listener: NoteItemClickListener
) :
    RecyclerView.Adapter<NoteListAdapter.NoteItemViewHolder>() {

    private var items: MutableList<NoteEntity> = mutableListOf()

    fun setItems(items: List<NoteEntity>) {
        clearItems()
        addItems(items)
        notifyDataSetChanged()
    }

    fun addItems(items: List<NoteEntity>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val binding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteItemViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class NoteItemViewHolder(
        private val binding: ItemNoteBinding,
        private val listener: NoteItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: NoteEntity) {
            with(item) {
                binding.tvTitleNote.text = title
                binding.tvContentNote.text = note
                binding.root.setOnClickListener {
                    listener.onEditMenuClicked(this)
                }
                binding.ivDelete.setOnClickListener {
                    listener.onDeleteMenuClicked(this)
                }
            }
        }
    }

}

interface NoteItemClickListener {
    fun onItemClicked(item: NoteEntity)
    fun onDeleteMenuClicked(item: NoteEntity)
    fun onEditMenuClicked(item: NoteEntity)
}