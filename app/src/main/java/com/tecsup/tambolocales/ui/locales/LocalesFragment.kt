package com.tecsup.tambolocales.ui.locales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tecsup.tambolocales.R
import com.tecsup.tambolocales.databinding.FragmentLocalesBinding
import com.tecsup.tambolocales.ui.LocalViewModel

class LocalesFragment : Fragment() {

    private var _binding: FragmentLocalesBinding? = null
    private val binding get() = _binding!!

    private val localViewModel: LocalViewModel by activityViewModels()
    private lateinit var adapter: LocalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = LocalesAdapter { local ->
            val bundle = bundleOf("localId" to local.id)
            findNavController().navigate(R.id.action_localesFragment_to_mapaLocalFragment, bundle)
        }
        binding.rvLocales.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAgregarLocal.setOnClickListener {
            findNavController().navigate(R.id.action_localesFragment_to_registroLocalDialogFragment)
        }
    }

    private fun observeViewModel() {
        localViewModel.allLocales.observe(viewLifecycleOwner) { lista ->
            adapter.submitList(lista)
            if (lista.isNullOrEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.rvLocales.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.rvLocales.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
