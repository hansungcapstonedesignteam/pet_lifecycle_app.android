package com.hansung.petlifetimecare.mapPackage

import com.google.gson.annotations.SerializedName
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "response", strict = false)
data class AnimalHospitalResponse(
    @field:Element(name = "list_total_count") val listTotalCount: Int,
    @field:Element(name = "RESULT") val result2: ResultInfo,
    @field:ElementList(inline = true, name = "row") val row: List<AnimalHospital>
)

@Root(name = "RESULT", strict = false)
data class ResultInfo @JvmOverloads constructor(
    @field:Element(name = "CODE") val CODE: String = "",
    @field:Element(name = "MESSAGE") val MESSAGE: String = ""
)


data class AnimalHospitalInfo(
    @SerializedName("SITEWHLADDR") val SITEWHLADDR: String,
    @SerializedName("RDNWHLADDR") val RDNWHLADDR: String,
    @SerializedName("SITETEL") val SITETEL: String
)
data class AnimalHospital(
    @field:Element(name = "OPNSFTEAMCODE") val OPNSFTEAMCODE: String,
    @field:Element(name = "MGTNO") val MGTNO: String,
    @field:Element(name = "APVPERMYMD") val APVPERMYMD: String,
    @field:Element(name = "APVCANCELYMD") val APVCANCELYMD: String?,
    @field:Element(name = "TRDSTATEGBN") val TRDSTATEGBN: String,
    @field:Element(name ="TRDSTATENM") val TRDSTATENM: String,
    @field:Element(name ="DTLSTATEGBN") val DTLSTATEGBN: String,
    @field:Element(name ="DTLSTATENM") val DTLSTATENM: String,
    @field:Element(name ="DCBYMD") val DCBYMD: String?,
    @field:Element(name ="CLGSTDT") val CLGSTDT: String?,
    @field:Element(name ="CLGENDDT") val CLGENDDT: String?,
    @field:Element(name ="ROPNYMD") val ROPNYMD: String?,
    @field:Element(name ="SITETEL") val SITETEL: String?,
    @field:Element(name ="SITEAREA") val SITEAREA: String?,
    @field:Element(name ="SITEPOSTNO") val SITEPOSTNO: String?,
    @field:Element(name ="SITEWHLADDR") val SITEWHLADDR: String,
    @field:Element(name ="RDNWHLADDR") val RDNWHLADDR: String,
    @field:Element(name ="RDNPOSTNO") val RDNPOSTNO: String,
    @field:Element(name ="BPLCNM") val BPLCNM: String,
    @field:Element(name ="LASTMODTS") val LASTMODTS: String,
    @field:Element(name ="UPDATEGBN") val UPDATEGBN: String,
    @field:Element(name ="UPDATEDT") val UPDATEDT: String,
    @field:Element(name ="UPTAENM") val UPTAENM: String?,
    @field:Element(name ="X") val X: Double,
    @field:Element(name ="Y") val Y: Double,
    @field:Element(name ="LINDJOBGBNNM") val LINDJOBGBNNM: String?,
    @field:Element(name ="LINDPRCBGBNNM") val LINDPRCBGBNNM: String?,
    @field:Element(name ="LINDSEQNO") val LINDSEQNO: String?,
    @field:Element(name ="RGTMBDSNO") val RGTMBDSNO: String?,
    @field:Element(name ="TOTEPNUM") val TOTEPNUM: String?
)

data class ResultData(
    val CODE: String,
    val MESSAGE: String
)
