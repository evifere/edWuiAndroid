package com.evifere.edwuiandroid.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.evifere.edwuiandroid.data.*
import com.evifere.edwuiandroid.json.JsonLoader
import java.io.IOException
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class MemoryViewModel(application: Application) : AndroidViewModel(application) {

    var cards = mutableStateListOf<CardModel>()
        private set

    var categories : List<DrawerCategory> = emptyList()

    private val _currentDeck = mutableStateOf<Deck?>(null)
    var currentDeck: State<Deck?> = _currentDeck

    var currentFileName : String = "json/memo/pair/003-decouverte_niv2.json"

    var currentIndex : Int = 2

    fun setDeck(deck: Deck) {
        _currentDeck.value = deck
    }

    var hasWon by mutableStateOf(false)
        private set

    public fun onVictory() {
        hasWon = true
    }

    public fun resetGame() {
        hasWon = false
    }

    init {
        loadCategories()
        loadFirstGame()
    }

    private fun loadCategories(){
        val context : Context = getApplication<Application>().applicationContext
        val files : List<String> = getJsonFilesFromAssets(context)

        categories = buildDrawerCategories(context,files)

    }
    public fun loadFirstGame() {
        loadDeck(currentFileName,currentIndex)
    }

    public fun loadDeck(fileName: String, index : Int){
        currentFileName = fileName
        currentIndex = index
        resetGame()
        val json = JsonLoader.loadJsonFromAssets(getApplication<Application>().applicationContext,fileName)
        val root = Gson().fromJson(json, Root::class.java)

        setDeck(root.board.decks.first().deck[index])

        val generatedCards = mutableListOf<CardModel>()
        var idCounter = 0

        currentDeck.value?.couple?.forEach { couple ->
            val couple_id = UUID.randomUUID().toString()
            couple.card.forEach { raw ->
                val image = extractImagePath(raw)
                val text = extractTextFromSpan(raw)
                val color = extractBackgroundColor(raw)
                generatedCards.add(
                    CardModel(
                        id = idCounter++,
                        imagePath = image,
                        text = text,
                        isFlipped = !(currentDeck.value?.metadata?.hideunselected ?: true) ,
                        color = color,
                        couple_id = couple_id
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

    public fun selectedCards(): List<CardModel>
    {
        return cards.filter {
            it.isSelected.value
        }
    }

    fun removeCouple() {

        val selected = selectedCards()

        // sécurité
        if (selected.size != 2) return

        val first = selected[0]
        val second = selected[1]

        // même couple -> suppression
        if (first.couple_id == second.couple_id) {

            cards.remove(first)
            cards.remove(second)

        } else {

            // pas le même couple -> désélection après 3 secondes
            viewModelScope.launch {

                first.isError.value = true
                second.isError.value = true

                delay(2000)
                first.isError.value = false
                second.isError.value = false

                first.isSelected.value = false
                second.isSelected.value = false

            }
        }

        if(cards.size == 0){
            onVictory()
        }
    }
}