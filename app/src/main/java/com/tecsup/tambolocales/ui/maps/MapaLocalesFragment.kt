package com.tecsup.tambolocales.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tecsup.tambolocales.R
import com.tecsup.tambolocales.data.local.LocalEntity
import com.tecsup.tambolocales.databinding.FragmentMapaLocalesBinding
import com.tecsup.tambolocales.ui.LocalViewModel
import java.util.Locale

class MapaLocalesFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapaLocalesBinding? = null
    private val binding get() = _binding!!

    private val localViewModel: LocalViewModel by activityViewModels()
    private var googleMap: GoogleMap? = null
    private val markerLocalMap = mutableMapOf<Marker, LocalEntity>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            enableMyLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapaLocalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapAll) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.fabAgregarLocalMap.setOnClickListener {
            findNavController().navigate(R.id.action_mapaLocalesFragment_to_registroLocalDialogFragment)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        checkLocationPermissionAndEnable()
        setupMapListeners()
        observeLocales()
    }

    private fun setupMapListeners() {
        googleMap?.setOnMarkerClickListener { marker ->
            val local = markerLocalMap[marker]
            if (local != null) {
                mostrarDetalleCard(local)
            }
            true
        }

        googleMap?.setOnMapClickListener {
            ocultarDetalleCard()
        }

        googleMap?.setOnMapLongClickListener { latLng ->
            val coordsStr = String.format(Locale.US, "%.5f, %.5f", latLng.latitude, latLng.longitude)
            val bundle = bundleOf("coordenadas" to coordsStr)
            findNavController().navigate(
                R.id.action_mapaLocalesFragment_to_registroLocalDialogFragment,
                bundle
            )
        }
    }

    private fun observeLocales() {
        localViewModel.allLocales.observe(viewLifecycleOwner) { lista ->
            googleMap?.clear()
            markerLocalMap.clear()

            if (lista.isNullOrEmpty()) {
                ocultarDetalleCard()
                return@observe
            }

            val builder = LatLngBounds.Builder()

            for (local in lista) {
                val position = LatLng(local.latitud, local.longitud)
                val marker = googleMap?.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(local.nombre)
                        .snippet(local.direccion)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
                if (marker != null) {
                    markerLocalMap[marker] = local
                }
                builder.include(position)
            }

            if (lista.size == 1) {
                val singlePos = LatLng(lista[0].latitud, lista[0].longitud)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(singlePos, 15f))
            } else {
                try {
                    val bounds = builder.build()
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120))
                } catch (_: Exception) {
                    val fallback = LatLng(-12.0705, -77.0510)
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(fallback, 13f))
                }
            }
        }
    }

    private fun mostrarDetalleCard(local: LocalEntity) {
        binding.tvCardNombre.text = local.nombre
        binding.tvCardDireccion.text = local.direccion
        binding.tvCardDiasAtencion.text = getString(R.string.horario_atencion_format, local.diasAtencion)
        binding.tvCardHorario.text = getString(R.string.horario_format, local.horario)
        binding.tvCardGerente.text = getString(R.string.gerente_format, local.gerente)

        binding.cardDetailLocal.visibility = View.VISIBLE
    }

    private fun ocultarDetalleCard() {
        binding.cardDetailLocal.visibility = View.GONE
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
