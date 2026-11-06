package com.tecsup.tambolocales.ui.locales

import androidx.recyclerview.widget.RecyclerView
import com.tecsup.tambolocales.R
import com.tecsup.tambolocales.data.local.LocalEntity
import com.tecsup.tambolocales.databinding.ItemLocalBinding

class LocalViewHolder(
    private val binding: ItemLocalBinding,
    private val onItemClick: (LocalEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(local: LocalEntity) {
        binding.tvNombre.text = local.nombre
        binding.tvDireccion.text = local.direccion
        binding.tvDiasAtencion.text = local.diasAtencion
        binding.tvHorario.text = local.horario
        binding.tvGerente.text = binding.root.context.getString(R.string.gerente_format, local.gerente)

        binding.root.setOnClickListener {
            onItemClick(local)
        }
    }
}
