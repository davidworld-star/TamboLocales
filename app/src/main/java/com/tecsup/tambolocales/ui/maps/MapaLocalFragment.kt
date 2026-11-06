package com.tecsup.tambolocales.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tecsup.tambolocales.R
import com.tecsup.tambolocales.databinding.FragmentMapaLocalBinding
import com.tecsup.tambolocales.ui.LocalViewModel

class MapaLocalFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapaLocalBinding? = null
    private val binding get() = _binding!!

    private val localViewModel: LocalViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null
    private var localId: Long = -1L

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            enableMyLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localId = arguments?.getLong("localId", -1L) ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapaLocalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNombreCorto.paintFlags = binding.tvNombreCorto.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapSingle) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        checkLocationPermissionAndEnable()
        observeLocal()
    }

    private fun observeLocal() {
        if (localId == -1L) return
        localViewModel.getLocalById(localId).observe(viewLifecycleOwner) { local ->
            local?.let {
                val position = LatLng(it.latitud, it.longitud)
                googleMap?.clear()
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(it.nombre)
                        .snippet(it.direccion)
                )
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17f))

                val nombreCortoFormatted = obtenerNombreCorto(it.nombre)
                binding.tvNombreCorto.text = nombreCortoFormatted
            }
        }
    }

    private fun obtenerNombreCorto(nombreCompleto: String): String {
        val sinTambo = nombreCompleto.replace("TAMBO", "", ignoreCase = true).trim()
        val partes = sinTambo.split("-")
        val nombreSub = partes.firstOrNull()?.trim() ?: sinTambo
        val primeraPalabra = nombreSub.split(" ").firstOrNull { it.isNotBlank() } ?: nombreSub
        val nombreLindo = primeraPalabra.lowercase().replaceFirstChar { it.uppercase() }
        return getString(R.string.tambo_prefix, nombreLindo)
    }

    private fun checkLocationPermissionAndEnable() {
        val fineGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            enableMyLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun enableMyLocation() {
        try {
            googleMap?.isMyLocationEnabled = true
        } catch (_: SecurityException) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
