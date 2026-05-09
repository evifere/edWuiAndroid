package com.evifere.edwuiandroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evifere.edwuiandroid.data.CardModel
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryScreen(viewModel: MemoryViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet (drawerContainerColor=androidx.compose.ui.graphics.Color.Transparent,
                modifier = Modifier
                    .background(Brush.horizontalGradient(
                        listOf(
                            Color(0xFF0275DA),
                            Color(0xFF001F54)
                        )
                    ))
                ) {
                DrawerContent(
                    viewModel.categories,
                    viewModel,
                    drawerState
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("EdWui") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF001F54),
                                    Color(0xFF0275DA)
                                )
                            )
                        ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            }
        ) {
            padding ->

            Box(
                modifier = Modifier
                    .padding(padding)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF67ace9),
                                Color(0xFFcce3f8)
                            )
                        )
                    )
                    .fillMaxSize()
            ) {
                val cards = viewModel.cards

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(cards.isEmpty()){
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.loadFirstGame() }
                        ) {
                            Text("Rejouer")
                        }
                        return@Box
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(viewModel.currentDeck.value?.metadata?.name  ?: "",fontWeight = FontWeight.Bold,
                        fontSize = 24.sp, color = Color(0xFF000000))

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(cards.size) { index ->
                            val card = cards[index]
                            MemoryCard(card, onClick = { card ->
                                val selectedCards = viewModel.selectedCards()
                                    // si déjà sélectionnée → on peut désélectionner
                                if (card.isSelected.value) {
                                    card.isSelected.value = false
                                    return@MemoryCard
                                }

                                // limite à 2 cartes
                                if (selectedCards.size >= 2) {
                                    return@MemoryCard
                                }

                                // sélection
                                card.isSelected.value = true

                                viewModel.removeCouple()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}