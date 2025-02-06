package com.example.examencasa2eva

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FragmentoMapa : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private var lati: Double = 0.0
    private var longi: Double = 0.0
    private var mapaListo = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmento_mapa, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val ubicacion = LatLng(41.65518, -4.72372) // Valladolid
        mMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación por defecto"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))

        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        mapaListo = true
    }

    fun actualizarTexto(datos1: Double, datos2: Double) {
        lati = datos1
        longi = datos2

        if (mapaListo) {
            val nuevaUbicacion = LatLng(lati, longi)
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(nuevaUbicacion).title("Nueva ubicación"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nuevaUbicacion, 15f))

            val iesjulian = LatLng(41.6320851, -4.7590656)
            mMap.addMarker(MarkerOptions().position(iesjulian).title("IES Julián Marías"))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}



