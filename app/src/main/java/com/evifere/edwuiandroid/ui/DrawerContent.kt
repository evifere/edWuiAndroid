package com.evifere.edwuiandroid.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import com.evifere.edwuiandroid.data.DrawerCategory

@Composable
fun DrawerContent(categories: List<DrawerCategory>, viewModel: MemoryViewModel, drawerState: DrawerState) {
    LazyColumn {
        items(categories.size) { index ->
            AccordionItem(categories[index],viewModel,drawerState)
        }
    }
}