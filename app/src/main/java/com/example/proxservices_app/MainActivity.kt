package com.example.proxservices_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proxservices_app.ui.navigation.NavGraph // <-- 1. Importa tu NavGraph
import com.example.proxservices_app.ui.theme.ProxServices_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProxServices_appTheme {
                //    Esto elimina el Scaffold y el Greeting de ejemplo.
                NavGraph()
            }
        }
    }
}
