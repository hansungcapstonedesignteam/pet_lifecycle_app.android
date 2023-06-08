package com.hansung.petlifetimecare.mapPackage


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.MapFragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hansung.petlifetimecare.R
import java.text.SimpleDateFormat
import java.util.*

class MapWalkActivity : AppCompatActivity(), LocationUpdatesListener {
    private lateinit var mapsFragment: MapsFragment
    private var lastLocation: Location? = null
    private var totalDistance = 0f
    private lateinit var distanceTextView: TextView

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_walk)

        distanceTextView = findViewById(R.id.distance_text_view)

        val chronometerView = findViewById<Chronometer>(R.id.chronometer_walk)
        val startButton = findViewById<Button>(R.id.startButton_walk)
        val endButton = findViewById<Button>(R.id.endButtion_walk)

        startButton.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(currentTime))

            // Save the formattedDate to Firebase
            val database = Firebase.database
            val myRef = database.getReference("walkStartTime")
            myRef.setValue(formattedDate)

            chronometerView.base = SystemClock.elapsedRealtime()
            chronometerView.start()
            startLocationUpdates()
        }


        endButton.setOnClickListener {
            chronometerView.stop()
            chronometerView.base = SystemClock.elapsedRealtime()
            stopLocationUpdates()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    lastLocation?.let {
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            it.latitude,
                            it.longitude,
                            location.latitude,
                            location.longitude,
                            results
                        )
                        totalDistance += results[0]
                        // Format the total distance to show only 2 decimal places
                        val formattedTotalDistance = String.format("%.2f", totalDistance)

                        // Set the formatted text to the TextView
                        distanceTextView.text = "${formattedTotalDistance}m"

                        // Set the text size to 30dp
                        distanceTextView.textSize = 30f
                    }
                    lastLocation = location
                }
            }
        }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { permission -> permission.value == true }) {
                loadMapFragment()
            } else {
                Toast.makeText(this, "권한을 허가해야 지도가 표시 됩니다.", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            loadMapFragment()
        }
    }

    private fun loadMapFragment() {
        mapsFragment = MapsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_walk_map, mapsFragment)
            .commit()
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun startLocationUpdates() {
        mapsFragment.startLocationUpdates()
        requestLocationUpdates()
    }

    override fun stopLocationUpdates() {
        mapsFragment.stopLocationUpdates()
        Intent(this, LocationUpdatesService::class.java).also { intent ->
            stopService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
