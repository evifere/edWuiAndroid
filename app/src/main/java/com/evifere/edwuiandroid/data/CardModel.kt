package com.evifere.edwuiandroid.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

data class CardModel(
    val id: Int,
    val imagePath: String,
    val text: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false,
    var isSelected: MutableState<Boolean> = mutableStateOf(false),
    var isError: MutableState<Boolean> = mutableStateOf(false),
    var color: Color,
    var couple_id : String
)