package com.example.juegopalabra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.juegopalabra.ui.theme.AdivinaPalabraTheme
import com.example.juegopalabra.view.MyApp
import com.example.juegopalabra.viewmodel.ViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModel()
        enableEdgeToEdge()
        setContent {
            AdivinaPalabraTheme {
                MyApp(viewModel)
            }
        }
    }
}