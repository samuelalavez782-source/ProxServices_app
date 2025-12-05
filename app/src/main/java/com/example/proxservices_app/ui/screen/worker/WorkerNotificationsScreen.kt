package com.example.proxservices_app.ui.screen.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.proxservices_app.ui.theme.Blanco
import com.example.proxservices_app.ui.theme.PrincipalAzul // Asegúrate de tener este color definido
import com.example.proxservices_app.R
// Asegúrate de que el paquete es correcto (ej. com.example.proxservices_app)

// Definición de las pestañas (secciones) de la notificación
enum class NotificationTab(val title: String) {
    TODO("Todo"),
    JOBS("Trabajos"),
    MESSAGES("Mensajes"),
    POINTS("Puntos")
}

@Composable
fun WorkerNotificationsScreen(navController: NavHostController) {
    // 1. Estado para saber qué pestaña está seleccionada
    var selectedTab by remember { mutableStateOf(NotificationTab.TODO) }
    val tabs = NotificationTab.entries.toTypedArray()

    Scaffold(
        // 2. Barra Superior (simulando tu diseño con botón de retroceso)
        topBar = { NotificationsTopBar(navController = navController) },
        containerColor = Blanco // Fondo blanco
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Blanco)
        ) {
            // 3. Contenedor del Título "Notificaciones" y las pestañas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blanco)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Notificaciones",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 4. Componente de las Pestañas (Tab Row)
                CustomNotificationTabRow(
                    tabs = tabs,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 5. Contenido Dinámico de la Pestaña
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)) // Fondo gris claro para el contenido
            ) {
                // Aquí llamamos a la función que dibuja la lista de notificaciones
                NotificationsContent(selectedTab = selectedTab)
            }
        }
    }
}

// --- Componente de la Top Bar ---
@Composable
fun NotificationsTopBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black
            )
        }
    }
}

// --- Componente de la Barra de Pestañas Personalizada ---
@Composable
fun CustomNotificationTabRow(
    tabs: Array<NotificationTab>,
    selectedTab: NotificationTab,
    onTabSelected: (NotificationTab) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        edgePadding = 0.dp,
        containerColor = Color.Transparent, // Fondo transparente
        indicator = { /* Indicador manual dentro del botón */ }
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab

            // Usamos un Box para poder aplicar el background de color al botón seleccionado
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(
                        color = if (isSelected) PrincipalAzul else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Tab(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier
                        .background(Color.Transparent),
                    text = {
                        Text(
                            text = tab.title,
                            color = if (isSelected) Blanco else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}

// --- Contenido Dinámico de las Notificaciones ---
@Composable
fun NotificationsContent(selectedTab: NotificationTab) {
    // Aquí es donde irá la lógica para cargar y mostrar las listas.
    // Usaremos listas de ejemplo por ahora.

    // Función de ayuda para generar datos de prueba
    val mockNotifications: List<NotificationItem> = when (selectedTab) {
        NotificationTab.TODO -> NotificationData.allNotifications
        NotificationTab.JOBS -> NotificationData.allNotifications.filter { it.type == NotificationType.JOB }
        NotificationTab.MESSAGES -> NotificationData.allNotifications.filter { it.type == NotificationType.MESSAGE }
        NotificationTab.POINTS -> NotificationData.allNotifications.filter { it.type == NotificationType.POINT }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (mockNotifications.isEmpty()) {
            item {
                Text(
                    text = "No hay notificaciones en esta sección.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    color = Color.Gray
                )
            }
        } else {
            items(mockNotifications) { notification ->
                NotificationCard(item = notification)
            }
        }
    }
}

// --- Modelos de Datos Simulados para Notificaciones ---

enum class NotificationType {
    JOB, MESSAGE, POINT
}

data class NotificationItem(
    val id: Int,
    val type: NotificationType,
    val title: String,
    val description: String?,
    val time: String,
    val iconId: Int,
    val iconColor: Color,
    val hasNewIndicator: Boolean = true
)

// Datos simulados (mock data) basados en tu imagen
object NotificationData {
    private val JobColor = Color(0xFF1976D2) // Azul oscuro
    private val MessageColor = Color(0xFF00BCD4) // Teal
    private val PointColor = Color(0xFFFFC107) // Amarillo

    val allNotifications = listOf(
        NotificationItem(
            id = 1,
            type = NotificationType.JOB,
            title = "Nuevo trabajo disponible",
            description = "Carlos envió una solicitud para un servici...",
            time = "Hace 2 hrs",
            iconId = R.drawable.ice_jobs, // Usamos un icono de trabajo que ya tienes
            iconColor = JobColor,
            hasNewIndicator = true
        ),
        NotificationItem(
            id = 2,
            type = NotificationType.MESSAGE,
            title = "Nuevo mensaje de Leo",
            description = "El cliente envió nuevas fotos del problema",
            time = "Hace 5 hrs",
            iconId = R.drawable.ic_messages, // Usamos el icono de mensaje
            iconColor = MessageColor,
            hasNewIndicator = true
        ),
        NotificationItem(
            id = 3,
            type = NotificationType.POINT,
            title = "Has ganado +200 pts",
            description = "Tu calificación promedio ha mejorado",
            time = "11:45",
            iconId = R.drawable.ic_puntos, // Usamos el icono de puntos
            iconColor = PointColor,
            hasNewIndicator = false
        )
    )
}

// --- Componente de la Tarjeta de Notificación Individual ---

@Composable
fun NotificationCard(item: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Lógica para ver el detalle de la notificación */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(item.iconColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = item.iconId),
                        contentDescription = item.title,
                        tint = item.iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Título y Descripción
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    item.description?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Hora e Indicador de "Nueva"
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.time,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (item.hasNewIndicator) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(PrincipalAzul, shape = RoundedCornerShape(3.dp))
                    )
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun WorkerNotificationsScreenPreview() {
    WorkerNotificationsScreen(navController = rememberNavController())
}