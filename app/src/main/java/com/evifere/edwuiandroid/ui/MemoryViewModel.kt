package com.evifere.edwuiandroid.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import com.evifere.edwuiandroid.data.*
import com.evifere.edwuiandroid.json.JsonLoader
import java.io.IOException
import androidx.compose.ui.graphics.Color
class MemoryViewModel(application: Application) : AndroidViewModel(application) {

    var cards = mutableStateListOf<CardModel>()
        private set

    var categories : List<DrawerCategory> = emptyList()

    init {
        loadCategories()
        loadFirstGame()
    }

    private fun loadCategories(){
        val context : Context = getApplication<Application>().applicationContext
        val files : List<String> = getJsonFilesFromAssets(context)

        categories = buildDrawerCategories(context,files)

    }
    private fun loadFirstGame() {
        loadDeck("json/memo/pair/003-decouverte_niv2.json",2)
    }

    public fun loadDeck(fileName: String, index : Int){
        val json = JsonLoader.loadJsonFromAssets(getApplication<Application>().applicationContext,fileName)
        val root = Gson().fromJson(json, Root::class.java)

        val deck = root.board.decks.first().deck[index]

        val generatedCards = mutableListOf<CardModel>()
        var idCounter = 0

        deck.couple.forEach { couple ->
            couple.card.forEach { raw ->
                val image = extractImagePath(raw)
                val text = extractTextFromSpan(raw)
                val color = extractBackgroundColor(raw)
                generatedCards.add(
                    CardModel(
                        id = idCounter++,
                        imagePath = image,
                        text = text,
                        isFlipped = !deck.metadata.hideunselected,
                        color = color
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

    private fun extractBackgroundColor(html: String): Color {
        val regex = Regex("""background-color:\s*([^;"]+)""")
        val match = regex.find(html)

        val colorName = match?.groupValues?.get(1)?.trim()

        return when (colorName?.lowercase()) {
            "red" -> Color.Red
            "blue" -> Color.Blue
            "green" -> Color.Green
            "black" -> Color.Black
            "white" -> Color.White
            "yellow" -> Color.Yellow
            "purple" -> Color(0xFF800080)
            else -> Color(0xFFF5E6A3) // jaune paille
        }
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
                title = root.board.title[0],
                filePath = jsonName,
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