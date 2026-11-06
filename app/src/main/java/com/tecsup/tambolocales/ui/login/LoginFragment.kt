package com.tecsup.tambolocales.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tecsup.tambolocales.R
import com.tecsup.tambolocales.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnIngresar.setOnClickListener {
            val correo = binding.etCorreo.text.toString()
            val clave = binding.etClave.text.toString()
            viewModel.login(correo, clave)
        }

        binding.btnRecuperar.setOnClickListener {
            mostrarDialogoRecuperar()
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginViewModel.LoginResult.Success -> {
                    viewModel.clearResult()
                    findNavController().navigate(R.id.action_loginFragment_to_controlFragment)
                }
                is LoginViewModel.LoginResult.Error -> {
                    val mensaje = getString(result.messageResId)
                    Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                    viewModel.clearResult()
                }
                null -> {}
            }
        }
    }

    private fun mostrarDialogoRecuperar() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_recover_title)
            .setMessage(R.string.dialog_recover_message)
            .setPositiveButton(R.string.dialog_recover_ok) { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    R.string.dialog_recover_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
