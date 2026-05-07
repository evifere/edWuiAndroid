package com.evifere.edwuiandroid.data

import androidx.compose.ui.graphics.Color

data class CardModel(
    val id: Int,
    val imagePath: String,
    val text: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false,
    var color: Color
)