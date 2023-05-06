package com.hansung.petlifetimecare.mapPackage

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "LOCALDATA_020301", strict = false)
data class LocalData020301 @JvmOverloads constructor(
    @field:Element(name = "list_total_count", required = false)
    @param:Element(name = "list_total_count", required = false)
    val listTotalCount: Int = 0,

    @field:ElementList(entry = "row", inline = true, required = false)
    @param:ElementList(entry = "row", inline = true, required = false)
    val rows: List<Row>? = null
) {
    @Root(name = "row", strict = false)
    data class Row @JvmOverloads constructor(

        @field:Element(name = "SITETEL", required = false)
        @param:Element(name = "SITETEL", required = false)
        val siteTel: String? = null,

        @field:Element(name = "BPLCNM", required = false)
        @param:Element(name = "BPLCNM", required = false)
        val bplcNm: String? = null,

        @field:Element(name = "SITEWHLADDR", required = false)
        @param:Element(name = "SITEWHLADDR", required = false)
        val siteWhlAddr: String? = null,

        @field:Element(name = "RDNWHLADDR", required = false)
        @param:Element(name = "RDNWHLADDR", required = false)
        val rdnWhlAddr: String? = null,

        @field:Element(name = "X", required = false)
       @param:Element(name = "X", required = false)
       val X: String? = null,

    @field:Element(name = "Y", required = false)
    @param:Element(name = "Y", required = false)
    val Y: String? = null

    )
}
