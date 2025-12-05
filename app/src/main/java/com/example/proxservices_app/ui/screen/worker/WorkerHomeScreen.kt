package com.example.proxservices_app.ui.screen.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource // <--- Importación necesaria
import com.example.proxservices_app.R // <--- Asegúrate de tener R.drawable.ic_bell

// Definición de colores base (simulación de tu tema)
val Teal500 = Color(0xFF00BCD4)
val Teal700 = Color(0xFF0097A7)
val CardBackground = Color(0xFFFFFFFF)
val PrimaryText = Color(0xFF333333)
val SecondaryText = Color(0xFF757575)

// Modelo de datos simulado para un servicio pendiente
data class PendingService(
    val id: Int,
    val clientName: String,
    val serviceType: String,
    val price: String,
    val time: String,
    val initials: String
)

// Datos simulados (mock data)
val mockServices = listOf(
    PendingService(1, "Marica González", "Limpieza del hogar", "$250", "14:30", "MG"),
    PendingService(2, "JH", "Limpieza del hogar", "$250", "14:30", "JH"),
    PendingService(3, "Karla González", "Limpieza del hogar", "$250", "14:30", "KG")
)

// --- FUNCIÓN PRINCIPAL (ACTUALIZADA) ---

/**
 * Pantalla de inicio para el Trabajador.
 *
 * @param onNavigateToConfirmation Callback para navegar a la pantalla de confirmación.
 * @param onNavigateToNotifications Callback para navegar a la pantalla de notificaciones.
 */
@Composable
fun WorkerHomeScreen(
    onNavigateToConfirmation: (jobId: String) -> Unit,
    onNavigateToNotifications: () -> Unit // <--- ¡NUEVO CALLBACK DE NOTIFICACIONES!
) {
    var isAvailable by remember { mutableStateOf(true) }

    Scaffold(
        // Llamada a WorkerTopBar actualizada con el nuevo callback
        topBar = { WorkerTopBar(onNotificationsClicked = onNavigateToNotifications) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Cabecera y Bienvenida
            item {
                Text(
                    text = "Hola de nuevo Carlos",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "¿Listo para trabajar hoy?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SecondaryText,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 2. Estado del Trabajador (Toggle de Disponibilidad)
            item {
                AvailabilityToggleCard(
                    isAvailable = isAvailable,
                    onToggle = { isAvailable = it }
                )
            }

            // 3. Tarjetas de Estadísticas
            item {
                WorkerStatsRow()
            }

            // 4. Próximo Servicio
            item {
                Text(
                    text = "Próximo Servicio",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                NextServiceCard()
            }

            // 5. Servicios Pendientes de Aceptar/Rechazar
            items(mockServices) { service ->
                PendingServiceCard(
                    service = service,
                    // CONEXIÓN: Pasa el callback de confirmación
                    onAcceptClicked = onNavigateToConfirmation
                )
            }
        }
    }
}

// --- Componentes Reutilizables (WorkerTopBar ACTUALIZADA) ---

@Composable
fun WorkerTopBar(onNotificationsClicked: () -> Unit) { // <--- ¡Firma actualizada!
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono de menú
        IconButton(onClick = { /* Navegar a Drawer/Menú */ }) {
            Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = PrimaryText)
        }

        // Espacio para empujar los iconos a la derecha
        Spacer(Modifier.weight(1f))

        // Icono de Notificaciones (¡NUEVO!)
        IconButton(onClick = onNotificationsClicked) { // <--- ¡Conexión!
            Icon(
                painter = painterResource(id = R.drawable.ic_bell),
                contentDescription = "Notificaciones",
                tint = PrimaryText,
                modifier = Modifier.size(30.dp)
            )
        }

        // Avatar del usuario
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFB74D))
                .clickable { /* Navegar a perfil */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "J",
                color = PrimaryText,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

// --- RESTO DE COMPONENTES (sin cambios funcionales) ---

@Composable
fun AvailabilityToggleCard(isAvailable: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Teal500),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Estado del Trabajador",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = CardBackground
                )
                Text(
                    text = if (isAvailable) "Activa tu disponibilidad para recibir solicitudes de trabajo." else "Estás inactivo. Activa tu estado para recibir trabajos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CardBackground
                )
            }
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = isAvailable,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = CardBackground,
                    checkedThumbColor = Teal500,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    uncheckedThumbColor = SecondaryText
                )
            )
        }
    }
}

@Composable
fun WorkerStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatCard(value = "5", label = "Pendientes", color = Color(0xFFFFCC80))
        StatCard(value = "2", label = "En Curso", color = Color(0xFF81D4FA))
        StatCard(value = "47", label = "Completados", color = Color(0xFF66BB6A))
    }
}

@Composable
fun StatCard(value: String, label: String, color: Color) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(90.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }
    }
}

@Composable
fun NextServiceCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarInitials("AG", color = Color(0xFF4DB6AC))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Ana García", fontWeight = FontWeight.SemiBold, color = PrimaryText)
                    Text("Limpieza del hogar", fontSize = 12.sp, color = SecondaryText)
                    Text("Hoy 15:30 • Roma Norte", fontSize = 12.sp, color = SecondaryText)
                }
            }
            TextButton(onClick = { /* Navegar a detalles del servicio */ }) {
                Text("Ver detalles", color = Teal500)
            }
        }
    }
}

@Composable
fun PendingServiceCard(
    service: PendingService,
    onAcceptClicked: (jobId: String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila de Información
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarInitials(service.initials, color = Teal500)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(service.clientName, fontWeight = FontWeight.SemiBold, color = PrimaryText)
                        Text(service.serviceType, fontSize = 12.sp, color = SecondaryText)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(service.price, fontWeight = FontWeight.Bold, color = PrimaryText)
                    Text(service.time, fontSize = 12.sp, color = SecondaryText)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Fila de Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAcceptClicked(service.id.toString()) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Teal500),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Aceptar")
                }
                OutlinedButton(
                    onClick = { /* Lógica para Rechazar */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(
                        SecondaryText
                    )
                    )
                ) {
                    Text("Rechazar", color = SecondaryText)
                }
            }
        }
    }
}

@Composable
fun AvatarInitials(initials: String, color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

// Preview para Android Studio
@Preview(showBackground = true)
@Composable
fun WorkerHomeScreenPreview() {
    WorkerHomeScreen(
        onNavigateToConfirmation = { /* Navegando a Job ID: $it */ },
        onNavigateToNotifications = { /* Navegando a Notificaciones */ } // Callback dummy para Preview
    )
}