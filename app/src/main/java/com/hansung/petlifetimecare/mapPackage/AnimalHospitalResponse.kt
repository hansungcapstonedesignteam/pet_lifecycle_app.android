package com.hansung.petlifetimecare.mapPackage

import com.google.gson.annotations.SerializedName

data class AnimalHospitalResponse(
    @SerializedName("list_total_count") val listTotalCount: Int,
    @SerializedName("RESULT") val result: ResultInfo,
    @SerializedName("row") val row: List<AnimalHospital>
)


data class ResultInfo(
    @SerializedName("CODE") val code: String,
    @SerializedName("MESSAGE") val message: String
)

data class AnimalHospitalInfo(
    @SerializedName("SITEWHLADDR") val SITEWHLADDR: String,
    @SerializedName("RDNWHLADDR") val RDNWHLADDR: String,
    @SerializedName("SITETEL") val SITETEL: String
)

data class AnimalHospital(
    @SerializedName("BPLCNM") val BPLCNM: String, // 사업장명
    @SerializedName("X") val X: String, // 좌표정보(X)
    @SerializedName("Y") val Y: String, // 좌표정보(Y)
    @SerializedName("SITETEL") val SITETEL: String, // 전화번호
    @SerializedName("SITEWHLADDR") val SITEWHLADDR: String, // 도로명주소
    @SerializedName("RDNWHLADDR") val RDNWHLADDR: String // 지번주소
)
