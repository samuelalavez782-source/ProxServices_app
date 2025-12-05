package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Asegúrate de que estos imports apunten a tus archivos de definición (Servicio y Colores)
import com.example.proxservices_app.ui.screen.client.Servicio
import com.example.proxservices_app.ui.screen.client.TextGray
import com.example.proxservices_app.ui.screen.client.StarYellow

@Composable
fun ReviewScreen(
    servicio: Servicio,
    onClose: () -> Unit,
    onSubmit: (rating: Int, comentario: String) -> Unit
) {
    // 1. Estado local para la reseña
    var rating by remember { mutableStateOf(5) }
    var comentario by remember { mutableStateOf("") }

    // 2. Pantalla modal: Fondo semitransparente + Tarjeta centrada
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClose), // Permite cerrar al hacer clic fuera
        contentAlignment = Alignment.Center
    ) {
        // Fondo oscuro
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black.copy(alpha = 0.4f)
        ) {}

        // Tarjeta de Reseña (detener la propagación del clic para evitar que se cierre)
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clickable(enabled = false, onClick = {}),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                // --- TÍTULO Y CERRAR ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Calificar a ${servicio.nombre}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        modifier = Modifier.clickable(onClick = onClose)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Servicio: ${servicio.oficio} (${servicio.identificador})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(24.dp))

                // --- SISTEMA DE RATING (Estrellas) ---
                Text(
                    text = "Tu puntuación: $rating de 5",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Estrella $i",
                            tint = if (i <= rating) StarYellow else TextGray.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(48.dp)
                                .padding(horizontal = 4.dp)
                                .clickable { rating = i }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- CAMPO DE COMENTARIO ---
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("¿Algún comentario sobre el servicio?") },
                    placeholder = { Text("El trabajador fue muy puntual...") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = false,
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTONES DE ACCIÓN ---
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onClose) {
                        Text("CANCELAR", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSubmit(rating, comentario) },
                        enabled = rating > 0, // No permitir enviar sin calificación
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("ENVIAR RESEÑA")
                    }
                }
            }
        }
    }
}