package com.hansung.petlifetimecare

data class BoardModel(
    var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var writer: String = "",
    var time: Long = 0,
    val imageUrl: String? = null
)
