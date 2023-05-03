package com.hansung.petlifetimecare.mapPackage.api

import com.hansung.petlifetimecare.mapPackage.AnimalHospitalResponse
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface AnimalHospitalApi {

    @GET("{apiKey}/{type}/{service}/{startIndex}/{endIndex}")

    fun getAnimalHospitals(
        @Path("apiKey") apiKey: String,
        @Path("type") type: String,
        @Path("service") service: String,
        @Path("startIndex") startIndex: Int,
        @Path("endIndex") endIndex: Int
    ): Call<AnimalHospitalResponse>


    companion object {
        private const val BASE_URL = "http://openapi.seoul.go.kr:8088/"

        fun create(): AnimalHospitalApi {
            val okHttpClient = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(AnimalHospitalApi::class.java)
        }
    }

}
