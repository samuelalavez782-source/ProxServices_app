package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClientHomeScreen(
    // 1. Recibe la función de navegación (onSearchClick)
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home del Cliente",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Botón que dispara la navegación al hacer clic.
        Button(
            onClick = onSearchClick // Llama a la acción que navega a Busqueda.kt
        ) {
            Text(text = "Buscar Servicios")
        }
    }
}