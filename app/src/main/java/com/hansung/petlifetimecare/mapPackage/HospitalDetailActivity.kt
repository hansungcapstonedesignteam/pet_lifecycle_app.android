package com.hansung.petlifetimecare.mapPackage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import com.hansung.petlifetimecare.R

class HospitalDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_hosdetail)

        // Intent에서 병원 정보 가져오기
        val hospitalName = intent.getStringExtra("hospitalName")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val address = intent.getStringExtra("address")
        val rating = intent.getFloatExtra("rating", 0f) // 별점 정보를 받아옵니다.

        // TextViews를 업데이트 합니다.
        findViewById<TextView>(R.id.hospital_title).text = hospitalName
        findViewById<TextView>(R.id.hospital_tellnumber).text = phoneNumber
        findViewById<TextView>(R.id.hospital_area).text = address
        findViewById<RatingBar>(R.id.rating_bar_detail).rating = rating
    }
}
