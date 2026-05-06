package com.evifere.edwuiandroid

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.evifere.edwuiandroid.json.JsonLoader
import androidx.activity.viewModels
import com.evifere.edwuiandroid.ui.MemoryScreen
import com.evifere.edwuiandroid.ui.theme.EdwuiAndroidTheme
import com.evifere.edwuiandroid.ui.MemoryViewModel
class MainActivity : ComponentActivity() {
    private val viewModel: MemoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EdwuiAndroidTheme {
                MemoryScreen(viewModel = viewModel)
            }
        }
    }
}
