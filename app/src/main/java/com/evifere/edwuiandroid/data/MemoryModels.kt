package com.evifere.edwuiandroid.data
import com.google.gson.annotations.SerializedName
data class Root(
    val board: Board
)

data class Board(
    val decks: List<DeckContainer>
)

data class DeckContainer(
    val deck: List<Deck>
)

data class DeckMetadata(
    val hideunselected: Boolean,
    val name: String
)
data class Deck(
    @SerializedName("$")
    val metadata: DeckMetadata,
    val couple: List<Couple>

)

data class Couple(
    val card: List<String>
)