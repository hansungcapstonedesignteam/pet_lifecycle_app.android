package com.hansung.petlifetimecare.mapPackage


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.MapFragment
import com.hansung.petlifetimecare.R

class MapWalkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_walk)

        val chronometerView = findViewById<Chronometer>(R.id.chronometer_walk)
        val startButton = findViewById<Button>(R.id.startButton_walk)
        val endButton = findViewById<Button>(R.id.endButtion_walk)

        startButton.setOnClickListener{
            chronometerView.base = SystemClock.elapsedRealtime()
            chronometerView.start()
        }

        endButton.setOnClickListener{
            chronometerView.stop()
            chronometerView.base = SystemClock.elapsedRealtime()
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
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_walk_map, MapsFragment())
            .commit()
    }
}

