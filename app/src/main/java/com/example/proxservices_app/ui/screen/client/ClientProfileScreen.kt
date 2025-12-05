package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxservices_app.R

// =====================================================================
//   COLORES ÚNICOS (NO CONFLICAN CON TU THEME)
// =====================================================================
val PerfilTurquesa = Color(0xFF00C7B9)
val PerfilTextoOscuro = Color(0xFF303030)
val PerfilBlanco = Color.White
val PerfilFondoAvatar = Color(0xFFFFB3A0)

// =====================================================================
//   SCREEN PRINCIPAL
// =====================================================================
@Composable
fun ClientProfileScreen(
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {}
) {

    val itemsPerfil = remember {
        listOf(
            PerfilItem(icon = Icons.Default.Person, title = "Nombre", value = "Juan Pérez"),
            PerfilItem(icon = Icons.Default.MailOutline, title = "Correo", value = "juan@gmail.com"),
            PerfilItem(icon = Icons.Default.Phone, title = "Teléfono", value = "55 1234 5678")
        )
    }

    Scaffold(
        topBar = {
            TopBarCliente(onBack = onBack, onEdit = onEdit)
        }
    ) { padding ->   // ← ESTO ES IMPORTANTE
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)   // ← APLICARLO ES OBLIGATORIO
                .padding(16.dp)
                .background(Color(0xFFF4F4F4)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FOTO + NOMBRE
            item {
                Spacer(modifier = Modifier.height(20.dp))
                FotoPerfilCliente()
                Spacer(modifier = Modifier.height(10.dp))
                NombreCliente()
                Spacer(modifier = Modifier.height(20.dp))
            }

            items(itemsPerfil) { item ->
                ItemPerfilCard(item)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                SeccionSeguridad()
            }
        }
    }
}


// =====================================================================
//   TOP BAR
// =====================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCliente(onBack: () -> Unit, onEdit: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Mi Perfil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = PerfilTurquesa)
            }
        }
    )
}

// =====================================================================
//   FOTO DE PERFIL
// =====================================================================
@Composable
fun FotoPerfilCliente() {
    Box(
        modifier = Modifier
            .size(130.dp) // un poco más grande
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        // Círculo de fondo
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(PerfilFondoAvatar)
        )

        // Imagen centrada sin recortes
        Image(
            painter = painterResource(id = R.drawable.logo_maria),
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Fit,     // <--- NO RECORTA
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
        )
    }
}


// =====================================================================
//   NOMBRE
// =====================================================================
@Composable
fun NombreCliente() {
    Text(
        text = "Juan Pérez",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = PerfilTextoOscuro,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

// =====================================================================
//   ITEM DEL PERFIL
// =====================================================================
@Composable
fun ItemPerfilCard(item: PerfilItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PerfilBlanco)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                item.icon,
                contentDescription = null,
                tint = PerfilTurquesa,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(item.title, fontWeight = FontWeight.Bold, color = PerfilTextoOscuro)
                Text(item.value, color = PerfilTextoOscuro.copy(alpha = 0.7f))
            }
        }
    }
}

// =====================================================================
//   SEGURIDAD
// =====================================================================
@Composable
fun SeccionSeguridad() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Seguridad",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = PerfilTextoOscuro
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PerfilBlanco),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = PerfilTurquesa,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text("Contraseña", fontWeight = FontWeight.Bold, color = PerfilTextoOscuro)
                    Text("**********", color = PerfilTextoOscuro.copy(alpha = 0.7f))
                }
            }
        }
    }
}

// =====================================================================
//   MODEL
// =====================================================================
data class PerfilItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val value: String
)
