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
import android.location.Location.distanceBetween
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
import java.lang.StrictMath.pow
import kotlin.math.*
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

private fun tm2LatLng(x: Double, y: Double): LatLng {
    val proj4KoreaCentral = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43"
    val proj4WGS84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs"

    val crsFactory = CRSFactory()
    val centralKoreaCRS = crsFactory.createFromParameters("centralKorea", proj4KoreaCentral)
    val wgs84CRS = crsFactory.createFromParameters("WGS84", proj4WGS84)

    val transformFactory = CoordinateTransformFactory()
    val transform = transformFactory.createTransform(centralKoreaCRS, wgs84CRS)

    val sourceCoordinate = ProjCoordinate(x, y)
    val targetCoordinate = ProjCoordinate()

    transform.transform(sourceCoordinate, targetCoordinate)

    val modifiedLatitude = targetCoordinate.y
    val modifiedLongitude = targetCoordinate.x

    return LatLng(modifiedLatitude, modifiedLongitude)
}


class Maps2Fragment : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private lateinit var providerClient: FusedLocationProviderClient
    private lateinit var apiClient: GoogleApiClient
    private var googleMap: GoogleMap? = null
    private val animalHospitalApi = AnimalHospitalApi.create()
    companion object {
        private const val TAG = "Maps2Fragment"
        private const val MIN_ZOOM_LEVEL = 14f
        private const val MAX_ZOOM_LEVEL = 50f
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
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        markerOptions.position(latLng)
        markerOptions.title("MyLocation")

        googleMap?.addMarker(markerOptions)
    }

    private var lastKnownLocation: Location? = null

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
                            lastKnownLocation = location
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
        animalHospitalApi.getAnimalHospitals(apiKey, "xml", "LOCALDATA_020301", 1, 100, "광진구")
            .enqueue(object : Callback<LocalData020301> {
                override fun onResponse(
                    call: Call<LocalData020301>,
                    response: Response<LocalData020301>
                ) {
                    if (response.isSuccessful) {
                        val totalCount = response.body()?.listTotalCount
                        Log.d(TAG, "API call response body: ${response.body()}")
                        response.body()?.rows?.forEach { hospital ->
                            hospital.X?.toDoubleOrNull()?.takeIf { it in 0.0..500000.0 }?.let { x ->
                                hospital.Y?.toDoubleOrNull()?.takeIf { it in 0.0..500000.0 }?.let { y ->
                                    val latLng = tm2LatLng(x, y)
                                    Log.d(TAG, "Original X: $x, Original Y: $y")
                                    Log.d(TAG, "Adding marker for hospital: ${hospital.bplcNm}, latitude: ${latLng.latitude}, longitude: ${latLng.longitude}")

                                    val markerOptions = MarkerOptions().apply {
                                        position(latLng)
                                        title(hospital.bplcNm)
                                        snippet("전화번호: ${hospital.siteTel}, 도로명주소: ${hospital.rdnWhlAddr}, 지번주소: ${hospital.siteWhlAddr}")
                                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    }
                                    googleMap?.addMarker(markerOptions)
                                } ?: run {
                                    Log.e(TAG, "Invalid Y coordinate for hospital: ${hospital.bplcNm}")
                                }
                            } ?: run {
                                Log.e(TAG, "Invalid X coordinate for hospital: ${hospital.bplcNm}")
                            }
                        }

                    } else {
                        Log.e(TAG, "API call failed, response error: ${response.errorBody()}")
                        // 실패한 경우 에러 처리
                    }
                }

                override fun onFailure(call: Call<LocalData020301>, t: Throwable) {
                    Log.e(TAG, "API call onFailure, error message: ${t.localizedMessage}")
                    t.printStackTrace() // 스택 추적을 출력하여 더 많은 정보를 얻습니다.
                }
            })
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
        googleMap.setOnCameraIdleListener {
            val zoomLevel = googleMap.cameraPosition.zoom
            if (zoomLevel >= MIN_ZOOM_LEVEL && zoomLevel <= MAX_ZOOM_LEVEL) {
                loadAnimalHospitalMarkers()
            } else {
                googleMap.clear()
            }
        }
        googleMap.setOnMarkerClickListener { marker ->
            val phoneNumber = marker.snippet!!.split("전화번호: ")[1].split(",")[0] // 마커의 snippet에서 전화번호를 추출
            val hospitalName = marker.title // 마커의 title에서 병원 이름을 가져옵니다.

            // MainActivity에게 전화번호와 병원 이름을 전달
            (activity as? MapHospitalActivity)?.apply {
                updatePhoneNumber(phoneNumber)
                if (hospitalName != null) {
                    updateHospitalName(hospitalName)
                } // 병원 이름을 전달합니다.
            }

            // 거리 업데이트
            lastKnownLocation?.let { location ->
                val markerPosition = marker.position
                val distance = distanceBetween(location.latitude, location.longitude, markerPosition.latitude, markerPosition.longitude)
                (activity as? MapHospitalActivity)?.updateDistance(distance)
            }
            marker.showInfoWindow() // 이 부분을 추가합니다.
            true
        }

    }
    private fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

}
