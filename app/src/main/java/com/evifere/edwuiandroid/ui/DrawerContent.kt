package com.evifere.edwuiandroid.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.evifere.edwuiandroid.data.DrawerCategory

@Composable
fun DrawerContent(categories: List<DrawerCategory>) {
    LazyColumn {
        items(categories.size) { index ->
            AccordionItem(categories[index])
        }
    }
}