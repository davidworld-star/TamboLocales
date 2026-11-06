package com.tecsup.tambolocales.ui.locales

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.tecsup.tambolocales.R
import com.tecsup.tambolocales.data.local.LocalEntity
import com.tecsup.tambolocales.databinding.DialogRegistroLocalBinding
import com.tecsup.tambolocales.ui.LocalViewModel

class RegistroLocalDialogFragment : DialogFragment() {

    private var _binding: DialogRegistroLocalBinding? = null
    private val binding get() = _binding!!

    private val localViewModel: LocalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        _binding = DialogRegistroLocalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argsCoordenadas = arguments?.getString("coordenadas")
        if (!argsCoordenadas.isNullOrBlank()) {
            binding.etCoordenadas.setText(argsCoordenadas)
        }

        binding.btnRegistrar.setOnClickListener {
            guardarLocal()
        }
    }

    private fun guardarLocal() {
        val nombre = binding.etNombreLocal.text.toString().trim()
        val gerente = binding.etGerente.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()
        val diasAtencion = binding.etDiasAtencion.text.toString().trim()
        val horario = binding.etHorario.text.toString().trim()
        val coordenadasStr = binding.etCoordenadas.text.toString().trim()

        var isValid = true

        if (nombre.isEmpty()) {
            binding.etNombreLocal.error = getString(R.string.error_campo_requerido)
            isValid = false
        }
        if (gerente.isEmpty()) {
            binding.etGerente.error = getString(R.string.error_campo_requerido)
            isValid = false
        }
        if (direccion.isEmpty()) {
            binding.etDireccion.error = getString(R.string.error_campo_requerido)
            isValid = false
        }
        if (diasAtencion.isEmpty()) {
            binding.etDiasAtencion.error = getString(R.string.error_campo_requerido)
            isValid = false
        }
        if (horario.isEmpty()) {
            binding.etHorario.error = getString(R.string.error_campo_requerido)
            isValid = false
        }

        if (coordenadasStr.isEmpty()) {
            binding.etCoordenadas.error = getString(R.string.error_campo_requerido)
            isValid = false
        }

        var latitud = 0.0
        var longitud = 0.0

        if (coordenadasStr.isNotEmpty()) {
            val parts = coordenadasStr.split(",")
            if (parts.size == 2) {
                val lat = parts[0].trim().toDoubleOrNull()
                val lon = parts[1].trim().toDoubleOrNull()
                if (lat != null && lon != null && lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0) {
                    latitud = lat
                    longitud = lon
                } else {
                    binding.etCoordenadas.error = getString(R.string.error_coordenadas_invalidas)
                    isValid = false
                }
            } else {
                binding.etCoordenadas.error = getString(R.string.error_coordenadas_invalidas)
                isValid = false
            }
        }

        if (!isValid) return

        val nuevoLocal = LocalEntity(
            nombre = nombre,
            direccion = direccion,
            gerente = gerente,
            diasAtencion = diasAtencion,
            horario = horario,
            latitud = latitud,
            longitud = longitud
        )

        localViewModel.insert(nuevoLocal)
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.92).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
