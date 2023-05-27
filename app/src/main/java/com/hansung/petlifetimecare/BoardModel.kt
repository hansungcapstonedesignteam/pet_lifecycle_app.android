package com.hansung.petlifetimecare

data class BoardModel (
    val contentsId: Int,
    val title: String = "",
    val contents: String = "",
    val userName: String = "",
    val userPassword: Int
)