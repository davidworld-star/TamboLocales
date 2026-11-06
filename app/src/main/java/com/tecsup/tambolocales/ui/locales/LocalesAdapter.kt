package com.tecsup.tambolocales.ui.locales

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tecsup.tambolocales.data.local.LocalEntity
import com.tecsup.tambolocales.databinding.ItemLocalBinding

class LocalesAdapter(
    private val onItemClick: (LocalEntity) -> Unit
) : ListAdapter<LocalEntity, LocalViewHolder>(LocalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val binding = ItemLocalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocalViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class LocalDiffCallback : DiffUtil.ItemCallback<LocalEntity>() {
        override fun areItemsTheSame(oldItem: LocalEntity, newItem: LocalEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocalEntity, newItem: LocalEntity): Boolean {
            return oldItem == newItem
        }
    }
}
