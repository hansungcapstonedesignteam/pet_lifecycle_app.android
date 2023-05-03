package com.hansung.petlifetimecare.mapPackage

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.hansung.petlifetimecare.R
import com.hansung.petlifetimecare.mapPackage.api.AnimalHospitalApi
import com.hansung.petlifetimecare.mapPackage.AnimalHospitalResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.errors.ApiException
import com.google.maps.model.GeocodingResult
import java.io.IOException

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ...

private fun getAddressLatLng(address: String, callback: (LatLng?) -> Unit) {
    val geoApiContext = GeoApiContext.Builder()
        .apiKey("AIzaSyAbd81Wz0KsfM4dj9eWMZTwMLvWzEVBtw4")
        .build()

    CoroutineScope(Dispatchers.Main).launch {
        val latLng = withContext(Dispatchers.IO) {
            try {
                val results: Array<GeocodingResult> = GeocodingApi.geocode(geoApiContext, address).await()
                if (results.isNotEmpty()) {
                    LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng)
                } else {
                    null
                }
            } catch (e: ApiException) {
                Log.e("GEOCODING_ERROR", "Geocoding API error: ${e.localizedMessage}") // 로그 추가
                null
            } catch (e: InterruptedException) {
                Log.e("GEOCODING_ERROR", "Geocoding interrupted: ${e.localizedMessage}") // 로그 추가
                null
            } catch (e: IOException) {
                Log.e("GEOCODING_ERROR", "Geocoding IO error: ${e.localizedMessage}") // 로그 추가
                null
            }
        }
        callback(latLng)
    }
}




class Maps2Fragment : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private lateinit var providerClient: FusedLocationProviderClient
    private lateinit var apiClient: GoogleApiClient
    private var googleMap: GoogleMap? = null
    private val animalHospitalApi = AnimalHospitalApi.create()

    companion object {
        private const val TAG = "Maps2Fragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps2, container, false)
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

        googleMap?.addMarker(markerOptions)
    }

    override fun onConnected(bundle: Bundle?) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
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

        loadAnimalHospitalMarkers() // 동물병원 위치를 로드합니다.
    }

    private fun loadAnimalHospitalMarkers() {
        val apiKey = "7052706f5371697a3731766b4a634d" // 발급받은 인증키로 변경
        animalHospitalApi.getAnimalHospitals(apiKey, "json", "LOCALDATA_020301", 1, 100)
            .enqueue(object : Callback<AnimalHospitalResponse> {
                override fun onResponse(
                    call: Call<AnimalHospitalResponse>,
                    response: Response<AnimalHospitalResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()?.result
                        Log.d(TAG, "API call Result Code: ${result?.code}, Result Message: ${result?.message}") // 요청 결과 코드 및 메시지 로그 출력

                        Log.d(TAG, "API call successful, response body: ${response.body()}") // 로그 추가
                        response.body()?.row?.forEach { hospital ->
                            val latitude = hospital.Y.toDoubleOrNull() // 위도 변환
                            val longitude = hospital.X.toDoubleOrNull() // 경도 변환

                            if (latitude != null && longitude != null) {
                                Log.d(TAG, "Adding marker for hospital: ${hospital.BPLCNM}, latitude: $latitude, longitude: $longitude") // 로그 추가

                                val latLng = LatLng(latitude, longitude)
                                val markerOptions = MarkerOptions().apply {
                                    position(latLng)
                                    title(hospital.BPLCNM)
                                    snippet("전화번호: ${hospital.SITETEL}, 도로명주소: ${hospital.SITEWHLADDR}, 지번주소: ${hospital.RDNWHLADDR}")
                                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                }
                                googleMap?.addMarker(markerOptions)
                            } else {
                                Log.e(TAG, "Invalid coordinates for hospital: ${hospital.BPLCNM}") // 로그 추가

                            }
                        }
                    } else {
                        Log.e(TAG, "API call failed, response error: ${response.errorBody()}") // 로그 추가

                        // 실패한 경우 에러 처리
                    }
                }

                override fun onFailure(call: Call<AnimalHospitalResponse>, t: Throwable) {
                    Log.e(TAG, "API call onFailure, error message: ${t.localizedMessage}") // 로그 추가
                    // 네트워크 오류 처리
                }

            })

    }

    private fun getAddressLatLng(address: String): LatLng? {
        val appContext = context
        return if (appContext != null) {
            try {
                val geocoder = Geocoder(appContext)
                val addressList = geocoder.getFromLocationName(address, 1)

                if (addressList?.isNotEmpty() == true) {
                    val location = addressList[0]
                    LatLng(location.latitude, location.longitude)
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("GEOCODER_ERROR", "Geocoder error: ${e.localizedMessage}") // 로그 추가
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }



    override fun onConnectionSuspended(cause: Int) {
        // Handle connection suspended
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        // Handle connection failed
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // 지도 스타일 적용
        try {
            val style = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
            googleMap.setMapStyle(style)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }
    }

}
