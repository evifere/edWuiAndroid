package com.evifere.edwuiandroid.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import com.evifere.edwuiandroid.data.*
import com.evifere.edwuiandroid.json.JsonLoader

class MemoryViewModel(application: Application) : AndroidViewModel(application) {

    var cards = mutableStateListOf<CardModel>()
        private set

    init {
        loadGame()
    }

    private fun loadGame() {
        val json = JsonLoader.loadJsonFromAssets(getApplication<Application>().applicationContext,"json/memo/pair/003-decouverte_niv2.json")
        val root = Gson().fromJson(json, Root::class.java)

        val firstDeck = root.board.decks.first().deck.first()

        val generatedCards = mutableListOf<CardModel>()
        var idCounter = 0

        firstDeck.couple.forEach { couple ->
            couple.card.forEach { raw ->
                val image = extractImagePath(raw)
                val text = extractTextFromSpan(raw)

                generatedCards.add(
                    CardModel(
                        id = idCounter++,
                        imagePath = image,
                        text = text,
                        isFlipped = !firstDeck.metadata.hideunselected
                    )
                )
            }
        }

        cards.clear()
        cards.addAll(generatedCards.shuffled())
    }

    private fun extractImagePath(html: String): String {
        val regex = """src="([^"]+)""""
        val match = Regex(regex).find(html)
        return match?.groupValues?.get(1)?.removePrefix("./") ?: ""
    }

    private fun extractTextFromSpan(html: String): String {
        val regex = Regex("""<span[^>]*>(.*?)</span>""")
        return regex.find(html)?.groupValues?.get(1) ?: ""
    }
}