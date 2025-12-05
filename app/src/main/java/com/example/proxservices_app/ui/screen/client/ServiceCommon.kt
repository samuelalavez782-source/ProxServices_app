package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxservices_app.R // Asegúrate de que esta importación sea correcta

// --- ENUMERACIÓN DE TABS ---


// --- COLORES ---
val WarningYellow = Color(0xFFFFC107)
val DarkBlueStatus = Color(0xFF003366)
val CompletedGreen = Color(0xFF00C890)
val CompletedBlue = Color(0xFF00BCD4)
val StarYellow = Color(0xFFFFC107)
val CardBackground = Color.White
val TextGray = Color.Gray
val TextBlack = Color.Black
val ScreenBackground = Color(0xFFF7F7F7)
/*
// --- MODELO DE DATOS UNIFICADO ---
data class Servicio(
    val id: String,
    val nombre: String,
    val oficio: String,
    val direccion: String,
    val identificador: String,
    val fechaHora: String,
    val tiempoEstimado: String? = null,
    val tiempoCompletadoHoras: Int? = null,
    val precioEstimado: String,
    val estado: ServiceTab,
    val rating: Float? = null,
    val horaInicio: String? = null,
    val avatarColor: Color = Color(0xFFE0E0E0)
)
*/
// --- FUNCIONES AUXILIARES ---

@Composable
fun WorkerAvatar(avatarColor: Color, isVerified: Boolean = true) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(avatarColor)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_limpieza),
            contentDescription = "Avatar",
            modifier = Modifier.fillMaxSize(0.6f).align(Alignment.Center),
            alignment = Alignment.Center
        )
        if (isVerified) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(PrimaryTurquoise)
                    .border(2.dp, CardBackground, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun DetailRow(iconRes: Int, text: String, color: Color = TextGray, fontWeight: FontWeight = FontWeight.Normal) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            // Nota: Se asume que R.drawable.ic_limpieza es un placeholder general.
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = color, fontWeight = fontWeight)
    }
}