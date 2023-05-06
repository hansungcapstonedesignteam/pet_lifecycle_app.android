package com.hansung.petlifetimecare.mapPackage.api

import com.hansung.petlifetimecare.mapPackage.AnimalHospitalResponse
import com.hansung.petlifetimecare.mapPackage.LocalData020301
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
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
        @Path("endIndex") endIndex: Int,
        @Query("SIGUN_NM") sigunNm: String
    ): Call<LocalData020301>


    companion object {
        private const val BASE_URL = "http://openapi.seoul.go.kr:8088/"

        fun create(): AnimalHospitalApi {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create()) // GsonConverterFactory 대신 SimpleXmlConverterFactory를 사용합니다.
                .build()

            return retrofit.create(AnimalHospitalApi::class.java)
        }
    }

}
