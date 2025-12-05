package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importa tus recursos y colores
import com.example.proxservices_app.R
// Importa tus colores de tema
import com.example.proxservices_app.ui.theme.GrisFondo
import com.example.proxservices_app.ui.theme.PrincipalAzul
import com.example.proxservices_app.ui.theme.GrisTextoSecundario

// Definimos los colores utilizados en el archivo
val AccentTurquesa = Color(0xFF00C7B9)

/* ---------------- DATOS DE MUESTRA ---------------- */

data class MessagePreview(
    val name: String,
    val message: String,
    val time: String,
    val avatarRes: Int,
    val unreadCount: Int? = null,
    val isOnline: Boolean = true
)

// Nota: Revisa que R.drawable.logo_usuario exista.
private val sampleMessages = listOf(
    MessagePreview(
        name = "Carlos Herrera",
        message = "Perfecto, estaré ahí a las 3 PM",
        time = "14:32",
        avatarRes = R.drawable.logo_carlos,
        unreadCount = 2,
        isOnline = true
    ),
    MessagePreview(
        name = "María González",
        message = "Gracias por el excelente servicio",
        time = "12:15",
        avatarRes = R.drawable.logo_maria,
        unreadCount = null,
        isOnline = true
    ),
    MessagePreview(
        name = "Luis Martínez",
        message = "¿Podrías venir mañana por la mañana?",
        time = "11:45",
        avatarRes = R.drawable.logo_luis,
        unreadCount = 1,
        isOnline = true
    ),
    MessagePreview(
        name = "Ana Rodríguez",
        message = "El trabajo quedó excelente, muchas gracias",
        time = "Ayer",
        avatarRes = R.drawable.logo_ana,
        unreadCount = null,
        isOnline = false
    ),
    MessagePreview(
        name = "Pedro Sánchez",
        message = "Necesito una cotización para instalación",
        time = "Ayer",
        avatarRes = R.drawable.logo_pedro,
        unreadCount = 3,
        isOnline = true
    )
)

/* ---------------- PANTALLA PRINCIPAL (CORREGIDA) ---------------- */

@Composable
fun ClientMessagesScreen(onNavigateToChat: (String) -> Unit) { // <-- ACEPTA EL CALLBACK
    Scaffold(
        topBar = { MessagesTopBar() },
        containerColor = GrisFondo
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleMessages) { message ->
                // PASA EL NOMBRE DEL CHAT AL HACER CLIC
                MessageCard(message, onClick = { onNavigateToChat(message.name) })
            }
        }
    }
}

/* ---------------- COMPONENTES ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesTopBar() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            ),
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mensajes",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PrincipalAzul
            )

            IconButton(onClick = { /* buscar */ }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color(0xFF8B8B8B),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}



// MODIFICADO PARA RECIBIR Y USAR EL CALLBACK
@Composable
fun MessageCard(message: MessagePreview, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AvatarWithIndicator(
                avatarRes = message.avatarRes,
                unreadCount = message.unreadCount,
                isOnline = message.isOnline
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = message.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = Color(0xFF2A2A2A),
                    maxLines = 1
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = message.message,
                    fontSize = 14.sp,
                    color = Color(0xFF7A7A7A),
                    maxLines = 1
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = message.time,
                    fontSize = 12.sp,
                    color = Color(0xFF9F9F9F)
                )
                Spacer(Modifier.height(6.dp))

                if (message.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF21D19F))
                    )
                }
            }
        }
    }
}


@Composable
fun AvatarWithIndicator(avatarRes: Int, unreadCount: Int?, isOnline: Boolean) {
    Box(modifier = Modifier.size(55.dp)) {

        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .shadow(4.dp, CircleShape)
        )

        // Mensajes no leídos
        unreadCount?.let { count ->
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(PrincipalAzul)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > 9) "9+" else count.toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Indicador online
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF21D19F))
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
