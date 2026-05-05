package com.evifere.edwuiandroid.data

data class CardModel(
    val id: Int,
    val imagePath: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)