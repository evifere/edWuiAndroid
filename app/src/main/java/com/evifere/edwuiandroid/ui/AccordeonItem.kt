package com.evifere.edwuiandroid.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evifere.edwuiandroid.data.DrawerCategory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

@Composable
fun AccordionItem(category: DrawerCategory, viewModel: MemoryViewModel, drawerState : DrawerState) {
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column {
        // 🔹 Titre principal (JSON)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Text(
                text = category.title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(if (expanded) "−" else "+")
        }

        // 🔹 Sous-catégories (Decks)
        AnimatedVisibility(visible = expanded) {
            Column {
                category.decks.forEachIndexed  { index,deckName ->
                    Text(
                        text = deckName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.loadDeck(category.title+".json",index)
                                scope.launch{drawerState.close()}
                            }
                            .padding(start = 32.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}