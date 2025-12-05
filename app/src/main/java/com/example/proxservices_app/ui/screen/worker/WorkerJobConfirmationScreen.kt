package com.example.proxservices_app.ui.screen.worker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.vector.ImageVector

// --- Simulación de datos para la pantalla ---
// NOTA: En un caso real, el jobId se usaría para buscar estos detalles.
data class JobDetails(
    val clientName: String = "Carlos Herrera",
    val status: String = "Cliente verificado",
    val serviceType: String = "Reparación de tubería",
    val requestedTime: String = "hace 10 minutos",
    val price: String = "350 MXN",
    val time: String = "Hoy 3:00 PM",
    val location: String = "1.5 km",
    val problemDescription: String = "La tubería debajo del lavamanos está goteando. Necesito reparación lo antes posible. El problema comenzó esta mañana y está empeorando.",
    val toolsNeeded: List<String> = listOf("Llave inglesa", "Sellador", "Cinta de teflón")
)

/**
 * Pantalla de Confirmación de Trabajo (Confirmar Trabajo).
 *
 * @param jobId El ID del trabajo que se aceptó en la pantalla anterior.
 * @param navController El controlador para volver a la pantalla anterior (mantido opcionalmente para popBackStack).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerJobConfirmationScreen(
    jobId: String, // CAMBIO CLAVE: Recibe el ID del trabajo
    navController: NavHostController // Mantenemos el navController si solo se usa para popBackStack
) {
    // NOTA: Aquí se cargaría el trabajo real usando el jobId:
    // val job = viewModel.getJobDetails(jobId)
    val job = JobDetails() // Simulación de carga
    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Confirmar Trabajo (ID: $jobId)", // Muestra el ID para confirmar
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onSurface,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = colors.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        modifier = Modifier.fillMaxSize().background(Color(0xFFEDEBE0))
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de Detalles del Cliente y Servicio
            item {
                ServiceDetailsCard(job)
            }

            // Sección de Ubicación del Cliente
            item {
                ClientLocationSection()
            }

            // Sección de Botones de Acción (Aceptar / Rechazar)
            item {
                ActionButtonsSection()
            }

            // Sección de Herramientas Necesarias
            item {
                ToolsNeededSection(job.toolsNeeded)
            }

            // Pie de página con información de privacidad
            item {
                PrivacyFooter()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- Componentes de la Pantalla --- (Tu código original sin cambios funcionales)

@Composable
fun ServiceDetailsCard(job: JobDetails) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cliente y Solicitud
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar del cliente (simulación de imagen o icono)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("CH", color = colors.onSecondary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(job.clientName, fontWeight = FontWeight.Bold, color = colors.onSurface, fontSize = 18.sp)
                    Text(job.status, fontSize = 14.sp, color = colors.primary)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Título del Servicio y Tiempo
            Text(job.serviceType, fontWeight = FontWeight.ExtraBold, color = colors.onSurface, fontSize = 24.sp)
            Text("Solicitado ${job.requestedTime}", fontSize = 14.sp, color = colors.onSurfaceVariant)

            Spacer(Modifier.height(20.dp))

            // Íconos de detalles (Presupuesto, Horario, Ubicación)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailIcon(icon = Icons.Filled.AttachMoney, label = "Presupuesto", value = job.price)
                DetailIcon(icon = Icons.Filled.Schedule, label = "Horario", value = job.time)
                DetailIcon(icon = Icons.Filled.LocationOn, label = "Ubicación", value = job.location)
            }

            Spacer(Modifier.height(20.dp))

            // Descripción del problema
            Text("Descripción del problema", fontWeight = FontWeight.SemiBold, color = colors.onSurface, modifier = Modifier.padding(bottom = 4.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant)
            ) {
                Text(
                    text = job.problemDescription,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 14.sp,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DetailIcon(icon: ImageVector, label: String, value: String) {
    val colors = MaterialTheme.colorScheme

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(90.dp)) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = colors.primary, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = colors.onSurfaceVariant, textAlign = TextAlign.Center)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.onSurface, textAlign = TextAlign.Center)
    }
}

@Composable
fun ClientLocationSection() {
    val colors = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Ubicación del Cliente", fontWeight = FontWeight.SemiBold, color = colors.onSurface, modifier = Modifier.padding(bottom = 8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            // Simulación de un mapa
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE0F7FA)),
                contentAlignment = Alignment.Center
            ) {
                Text("Mapa de Ubicación (Simulado)", color = colors.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ActionButtonsSection() {
    val colors = MaterialTheme.colorScheme

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { /* Lógica para Aceptar */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
        ) {
            Text("Aceptar Trabajo", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.onPrimary)
        }
        TextButton(
            onClick = { /* Lógica para Rechazar */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Rechazar", color = colors.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ToolsNeededSection(tools: List<String>) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Herramientas Necesarias", fontWeight = FontWeight.SemiBold, color = colors.onSurface, modifier = Modifier.padding(bottom = 8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tools.forEach { tool ->
                    Chip(tool = tool)
                }
            }
        }
    }
}

@Composable
fun Chip(tool: String) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(colors.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tool,
            color = colors.onPrimary,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun PrivacyFooter() {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "• Recibirás actualizaciones en tiempo real",
            fontSize = 12.sp,
            color = colors.onSurfaceVariant
        )
        Text(
            text = "• Tu información está protegida",
            fontSize = 12.sp,
            color = colors.onSurfaceVariant
        )
    }
}//fin