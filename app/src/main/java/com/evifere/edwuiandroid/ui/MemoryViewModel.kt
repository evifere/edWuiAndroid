package com.evifere.edwuiandroid.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import com.evifere.edwuiandroid.data.*
import com.evifere.edwuiandroid.json.JsonLoader
import com.evifere.edwuiandroid.json.JsonLoader.Companion.loadJsonFromAssets
import java.io.IOException

class MemoryViewModel(application: Application) : AndroidViewModel(application) {

    var cards = mutableStateListOf<CardModel>()
        private set

    var categories : List<DrawerCategory> = emptyList()

    init {
        loadCategories()
        loadGame()
    }

    private fun loadCategories(){
        val context : Context = getApplication<Application>().applicationContext
        val files : List<String> = getJsonFilesFromAssets(context)

        categories = buildDrawerCategories(context,files)

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

    private  fun buildDrawerCategories(context: Context, files: List<String>): List<DrawerCategory> {
        return files.map { fileName ->
            val json = JsonLoader.loadJsonFromAssets(context,
                fileName
            )
            val root = Gson().fromJson(json, Root::class.java)

            val jsonName = fileName.removeSuffix(".json")

            val decks = root.board.decks

            DrawerCategory(
                title = jsonName,
                decks = decks.flatMap { it -> it.deck.map { deck -> deck.metadata.name } }
            )
        }
    }

    private fun getJsonFilesFromAssets(context: Context): List<String> {
        val assetManager = context.assets
        val path = "json/memo/pair"

        return try {
            assetManager.list(path)
                ?.filter { it.endsWith(".json") }
                ?.map { "$path/$it" }
                ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}