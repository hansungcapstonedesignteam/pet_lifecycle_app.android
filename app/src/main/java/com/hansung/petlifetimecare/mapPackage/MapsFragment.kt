package com.hansung.petlifetimecare.mapPackage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.hansung.petlifetimecare.R


class MapsFragment : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private var currentMarker: Marker? = null
    private var googleMap: GoogleMap? = null
    private lateinit var providerClient: FusedLocationProviderClient
    private lateinit var apiClient: GoogleApiClient
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var polyline: Polyline

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val latitude = intent?.getDoubleExtra("Latitude", 0.0)
            val longitude = intent?.getDoubleExtra("Longitude", 0.0)
            val newLatLng = LatLng(latitude ?: 0.0, longitude ?: 0.0)
            polylineOptions.add(newLatLng)
            polyline.points = polylineOptions.points
            moveMap(latitude ?: 0.0, longitude ?: 0.0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        providerClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        apiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        apiClient.connect()

        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            receiver,
            IntentFilter("LocationUpdate")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(receiver)
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        val position: CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOptions.position(latLng)
        markerOptions.title("MyLocation")

        // Remove the old marker
        currentMarker?.remove()

        // Add a new marker and save its reference
        currentMarker = googleMap?.addMarker(markerOptions)
    }

    override fun onConnected(bundle: Bundle?) {
        if (context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED) {
            providerClient.lastLocation.addOnSuccessListener(
                requireActivity(),
                object : OnSuccessListener<Location> {
                    override fun onSuccess(location: Location?) {
                        location?.let {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            moveMap(latitude, longitude)
                        }
                    }
                }
            )
            apiClient.disconnect()
        }
    }

    override fun onConnectionSuspended(p0: Int) {}

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        polylineOptions = PolylineOptions().width(10f).color(Color.BLUE)
        polyline = googleMap?.addPolyline(polylineOptions) ?: return
    }

    fun startLocationUpdates() {
        if (context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000 // 10초마다 업데이트
                fastestInterval = 5000 // 최소 5초마다 업데이트
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            LocationServices.getFusedLocationProviderClient(requireActivity()).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    fun stopLocationUpdates() {
        if (apiClient.isConnected) {
            LocationServices.getFusedLocationProviderClient(requireActivity()).removeLocationUpdates(locationCallback)
            apiClient.disconnect()
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                val newLatLng = LatLng(location.latitude, location.longitude)
                polylineOptions.add(newLatLng)
                polyline.points = polylineOptions.points
                moveMap(location.latitude, location.longitude)
            }
        }
    }
}

interface LocationUpdatesListener {
    fun startLocationUpdates()
    fun stopLocationUpdates()
}

