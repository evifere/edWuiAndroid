package com.evifere.edwuiandroid.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable

@Composable
fun MemoryScreen(viewModel: MemoryViewModel) {
    val cards = viewModel.cards

    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(cards.size) { index ->
            val card = cards[index]
            MemoryCard(card)
        }
    }
}