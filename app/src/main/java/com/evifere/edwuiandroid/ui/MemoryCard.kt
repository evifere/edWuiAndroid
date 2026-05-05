package com.evifere.edwuiandroid.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.evifere.edwuiandroid.data.CardModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun MemoryCard(
    card: CardModel,
    onClick: (CardModel) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f) // carré
            .clickable { onClick(card) },
        shape = RoundedCornerShape(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (card.isFlipped) Color(0xFFF5E6A3) // jaune paille
                    else Color(0xFF2196F3) // bleu
                ),
            contentAlignment = Alignment.Center
        ) {

            if (card.isFlipped) {

                if(card.imagePath.count() > 0){
                    val uri = Uri.parse("file:///android_asset/${card.imagePath}")
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(0.7f)
                    )
                }
                else {
                    Text(
                        text = card.text,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}