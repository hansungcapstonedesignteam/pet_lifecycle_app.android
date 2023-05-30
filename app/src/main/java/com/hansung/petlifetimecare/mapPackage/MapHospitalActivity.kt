package com.hansung.petlifetimecare.mapPackage


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.MapFragment
import com.hansung.petlifetimecare.R




class MapHospitalActivity : AppCompatActivity() {

    private var selectedPhoneNumber: String? = null
    private var selectedHospitalName: String? = null
    private var selectedAddress: String? = null
    private var selectedRating: Float? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_hospital)


        val infoButton: Button = findViewById(R.id.infoButton)
        infoButton.setOnClickListener {
            // 만약 선택된 병원 정보가 있으면, 그 정보를 사용해 handleMarkerClick을 호출
            if (selectedPhoneNumber != null && selectedHospitalName != null && selectedAddress != null && selectedRating != null) {
                startHospitalDetailActivity()
            } else {
                Toast.makeText(this, "병원을 선택해주세요.", Toast.LENGTH_SHORT).show()
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
    fun updateDistance(distance: Int) {
        val distanceText: TextView = findViewById(R.id.distance_text)
        distanceText.text = "거리: ${distance}m" // 거리를 미터로 표시
    }
    fun updateHospitalName(hospitalName: String) {
        val hospitalNameText: TextView = findViewById(R.id.hospitalName)
        hospitalNameText.text = hospitalName
    }

    fun updatePhoneNumber(phoneNumber: String) {
        val phoneText: TextView = findViewById(R.id.phone_text)
        phoneText.text = "전화: $phoneNumber"
    }
    private fun loadMapFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_hospital_map, Maps2Fragment())
            .commit()
    }

    fun handleMarkerClick(phoneNumber: String, hospitalName: String, address: String, rating: Float) {
        selectedPhoneNumber = phoneNumber
        selectedHospitalName = hospitalName
        selectedAddress = address
        selectedRating = rating
    }

    fun startHospitalDetailActivity() {
        val intent = Intent(this, HospitalDetailActivity::class.java).apply {
            putExtra("phoneNumber", selectedPhoneNumber)
            putExtra("hospitalName", selectedHospitalName)
            putExtra("address", selectedAddress)
            putExtra("rating", selectedRating)
        }
        startActivity(intent)
    }
}

